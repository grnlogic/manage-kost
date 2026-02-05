package MenejementKos.DatabaseKos.Service;

import MenejementKos.DatabaseKos.model.kamar;
import MenejementKos.DatabaseKos.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Mendapatkan semua kamar
     */
    public List<kamar> getAllRooms() {
        logger.info("Fetching all rooms");
        return roomRepository.findAll();
    }

    /**
     * Mendapatkan kamar berdasarkan ID
     */
    public kamar getRoomById(Long id) {
        logger.info("Fetching room with id: {}", id);
        return roomRepository.findById(id).orElse(null);
    }

    /**
     * Mendapatkan kamar berdasarkan nomor kamar
     */
    public Optional<kamar> getRoomByNomorKamar(String nomorKamar) {
        logger.info("Fetching room with nomor kamar: {}", nomorKamar);
        return roomRepository.findByNomorKamar(nomorKamar);
    }

    /**
     * Mendapatkan semua kamar yang tersedia (kosong)
     */
    public List<kamar> getAvailableRooms() {
        logger.info("Fetching available rooms");
        return roomRepository.findAvailableRooms();
    }

    /**
     * Mendapatkan kamar berdasarkan status
     */
    public List<kamar> getRoomsByStatus(String status) {
        logger.info("Fetching rooms with status: {}", status);
        return roomRepository.findByStatus(status);
    }

    /**
     * Mendapatkan kamar dengan harga maksimal tertentu
     */
    public List<kamar> getRoomsByMaxPrice(Double maxPrice) {
        logger.info("Fetching rooms with max price: {}", maxPrice);
        return roomRepository.findByHargaBulananLessThanEqual(maxPrice);
    }

    /**
     * Mendapatkan kamar dengan range harga
     */
    public List<kamar> getRoomsByPriceRange(Double minPrice, Double maxPrice) {
        logger.info("Fetching rooms with price range: {} - {}", minPrice, maxPrice);
        return roomRepository.findByHargaBulananBetween(minPrice, maxPrice);
    }

    /**
     * Mencari kamar berdasarkan fasilitas
     */
    public List<kamar> searchRoomsByFacility(String facility) {
        logger.info("Searching rooms with facility: {}", facility);
        return roomRepository.findByFasilitasContaining(facility);
    }

    /**
     * Menghitung jumlah kamar berdasarkan status
     */
    public Long countRoomsByStatus(String status) {
        logger.info("Counting rooms with status: {}", status);
        return roomRepository.countByStatus(status);
    }

    /**
     * Menyimpan atau update kamar
     * Validasi nomor kamar untuk insert
     */
    public kamar saveRoom(kamar room) {
        // Validasi untuk insert baru (id null)
        if (room.getId() == null) {
            if (roomRepository.existsByNomorKamar(room.getNomorKamar())) {
                logger.error("Room number already exists: {}", room.getNomorKamar());
                throw new IllegalArgumentException("Nomor kamar " + room.getNomorKamar() + " sudah ada!");
            }
            logger.info("Creating new room with nomor kamar: {}", room.getNomorKamar());
        } else {
            // Untuk update, cek apakah nomor kamar berubah
            Optional<kamar> existingRoom = roomRepository.findById(room.getId());
            if (existingRoom.isPresent()) {
                String oldNomorKamar = existingRoom.get().getNomorKamar();
                if (!oldNomorKamar.equals(room.getNomorKamar())) {
                    // Nomor kamar berubah, cek duplikasi
                    if (roomRepository.existsByNomorKamar(room.getNomorKamar())) {
                        logger.error("Room number already exists: {}", room.getNomorKamar());
                        throw new IllegalArgumentException("Nomor kamar " + room.getNomorKamar() + " sudah ada!");
                    }
                }
            }
            logger.info("Updating room with id: {}", room.getId());
        }
        
        // Set default values jika null
        if (room.getStatus() == null || room.getStatus().isEmpty()) {
            room.setStatus("kosong");
        }
        
        return roomRepository.save(room);
    }

    /**
     * Update status kamar
     */
    public kamar updateRoomStatus(Long id, String newStatus) {
        logger.info("Updating room status for id: {} to: {}", id, newStatus);
        kamar room = roomRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Kamar dengan ID " + id + " tidak ditemukan"));
        
        room.setStatus(newStatus);
        return roomRepository.save(room);
    }

    /**
     * Update status pembayaran kamar
     */
    public kamar updatePaymentStatus(Long id, String paymentStatus) {
        logger.info("Updating payment status for room id: {} to: {}", id, paymentStatus);
        kamar room = roomRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Kamar dengan ID " + id + " tidak ditemukan"));
        
        room.setStatusPembayaran(paymentStatus);
        return roomRepository.save(room);
    }

    /**
     * Menghapus kamar
     */
    public void deleteRoom(Long id) {
        logger.info("Deleting room with id: {}", id);
        if (!roomRepository.existsById(id)) {
            logger.error("Room not found with id: {}", id);
            throw new IllegalArgumentException("Kamar dengan ID " + id + " tidak ditemukan");
        }
        roomRepository.deleteById(id);
    }

    /**
     * Cek apakah nomor kamar sudah ada
     */
    public boolean isRoomNumberExists(String nomorKamar) {
        return roomRepository.existsByNomorKamar(nomorKamar);
    }

    /**
     * Get room statistics
     */
    public RoomStatistics getRoomStatistics() {
        logger.info("Fetching room statistics");
        RoomStatistics stats = new RoomStatistics();
        stats.setTotalRooms(roomRepository.count());
        stats.setAvailableRooms(roomRepository.countByStatus("kosong"));
        stats.setOccupiedRooms(roomRepository.countByStatus("terisi"));
        stats.setPendingRooms(roomRepository.countByStatus("pending"));
        return stats;
    }

    /**
     * Inner class untuk statistik kamar
     */
    public static class RoomStatistics {
        private Long totalRooms;
        private Long availableRooms;
        private Long occupiedRooms;
        private Long pendingRooms;

        // Getters and Setters
        public Long getTotalRooms() { return totalRooms; }
        public void setTotalRooms(Long totalRooms) { this.totalRooms = totalRooms; }
        
        public Long getAvailableRooms() { return availableRooms; }
        public void setAvailableRooms(Long availableRooms) { this.availableRooms = availableRooms; }
        
        public Long getOccupiedRooms() { return occupiedRooms; }
        public void setOccupiedRooms(Long occupiedRooms) { this.occupiedRooms = occupiedRooms; }
        
        public Long getPendingRooms() { return pendingRooms; }
        public void setPendingRooms(Long pendingRooms) { this.pendingRooms = pendingRooms; }
    }
}
