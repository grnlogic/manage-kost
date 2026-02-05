package MenejementKos.DatabaseKos.Controller;

import MenejementKos.DatabaseKos.Service.RoomService;
import MenejementKos.DatabaseKos.model.kamar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = {
    "http://141.11.25.167:3000", 
    "https://kos-app-frontend-rzng-beta.vercel.app",
    "https://vercel.com/geran357s-projects/kos-app-frontend-rzng/HLVtQC4FU14UkwetGQG8xkToB97P",
    "https://backend-kos-app.up.railway.app",
    "manage-kost-production.up.railway.app"
})
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    /**
     * GET /api/rooms - Mendapatkan semua kamar
     */
    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        try {
            logger.info("Request: Get all rooms");
            List<kamar> rooms = roomService.getAllRooms();
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            logger.error("Error getting all rooms: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data kamar: " + e.getMessage()));
        }
    }

    /**
     * GET /api/rooms/{id} - Mendapatkan kamar berdasarkan ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        try {
            logger.info("Request: Get room by id: {}", id);
            kamar room = roomService.getRoomById(id);
            if (room != null) {
                return ResponseEntity.ok(room);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Kamar dengan ID " + id + " tidak ditemukan"));
        } catch (Exception e) {
            logger.error("Error getting room by id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data kamar: " + e.getMessage()));
        }
    }

    /**
     * GET /api/rooms/nomor/{nomorKamar} - Mendapatkan kamar berdasarkan nomor kamar
     */
    @GetMapping("/nomor/{nomorKamar}")
    public ResponseEntity<?> getRoomByNomorKamar(@PathVariable String nomorKamar) {
        try {
            logger.info("Request: Get room by nomor kamar: {}", nomorKamar);
            return roomService.getRoomByNomorKamar(nomorKamar)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Kamar nomor " + nomorKamar + " tidak ditemukan")));
        } catch (Exception e) {
            logger.error("Error getting room by nomor kamar {}: {}", nomorKamar, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data kamar: " + e.getMessage()));
        }
    }

    /**
     * GET /api/rooms/available - Mendapatkan semua kamar yang tersedia
     */
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableRooms() {
        try {
            logger.info("Request: Get available rooms");
            List<kamar> rooms = roomService.getAvailableRooms();
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            logger.error("Error getting available rooms: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data kamar tersedia: " + e.getMessage()));
        }
    }

    /**
     * GET /api/rooms/status/{status} - Mendapatkan kamar berdasarkan status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getRoomsByStatus(@PathVariable String status) {
        try {
            logger.info("Request: Get rooms by status: {}", status);
            List<kamar> rooms = roomService.getRoomsByStatus(status);
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            logger.error("Error getting rooms by status {}: {}", status, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data kamar: " + e.getMessage()));
        }
    }

    /**
     * GET /api/rooms/search - Mencari kamar berdasarkan berbagai kriteria
     * Query params: maxPrice, minPrice, facility
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchRooms(
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) String facility) {
        try {
            logger.info("Request: Search rooms with maxPrice: {}, minPrice: {}, facility: {}", 
                maxPrice, minPrice, facility);
            
            List<kamar> rooms;
            
            if (facility != null && !facility.isEmpty()) {
                rooms = roomService.searchRoomsByFacility(facility);
            } else if (minPrice != null && maxPrice != null) {
                rooms = roomService.getRoomsByPriceRange(minPrice, maxPrice);
            } else if (maxPrice != null) {
                rooms = roomService.getRoomsByMaxPrice(maxPrice);
            } else {
                rooms = roomService.getAllRooms();
            }
            
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            logger.error("Error searching rooms: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mencari kamar: " + e.getMessage()));
        }
    }

    /**
     * GET /api/rooms/statistics - Mendapatkan statistik kamar
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getRoomStatistics() {
        try {
            logger.info("Request: Get room statistics");
            RoomService.RoomStatistics stats = roomService.getRoomStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error getting room statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil statistik kamar: " + e.getMessage()));
        }
    }

    /**
     * POST /api/rooms - Membuat kamar baru
     */
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody kamar room) {
        try {
            logger.info("Request: Create new room with nomor kamar: {}", room.getNomorKamar());
            
            // Validasi input
            if (room.getNomorKamar() == null || room.getNomorKamar().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Nomor kamar tidak boleh kosong"));
            }
            if (room.getHargaBulanan() <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Harga bulanan harus lebih dari 0"));
            }
            
            kamar savedRoom = roomService.saveRoom(room);
            logger.info("Room created successfully with id: {}", savedRoom.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error creating room: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating room: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal membuat kamar: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/rooms/{id} - Update kamar
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody kamar room) {
        try {
            logger.info("Request: Update room with id: {}", id);
            
            // Cek apakah kamar ada
            kamar existingRoom = roomService.getRoomById(id);
            if (existingRoom == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Kamar dengan ID " + id + " tidak ditemukan"));
            }
            
            room.setId(id);
            kamar updatedRoom = roomService.saveRoom(room);
            logger.info("Room updated successfully with id: {}", id);
            return ResponseEntity.ok(updatedRoom);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error updating room {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating room {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengupdate kamar: " + e.getMessage()));
        }
    }

    /**
     * PATCH /api/rooms/{id}/status - Update status kamar saja
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateRoomStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        try {
            logger.info("Request: Update room status for id: {} to: {}", id, status);
            
            // Validasi status
            if (!status.equals("kosong") && !status.equals("terisi") && !status.equals("pending")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Status harus 'kosong', 'terisi', atau 'pending'"));
            }
            
            kamar updatedRoom = roomService.updateRoomStatus(id, status);
            return ResponseEntity.ok(updatedRoom);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating room status for id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating room status {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengupdate status kamar: " + e.getMessage()));
        }
    }

    /**
     * PATCH /api/rooms/{id}/payment-status - Update status pembayaran
     */
    @PatchMapping("/{id}/payment-status")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable Long id, 
            @RequestParam String paymentStatus) {
        try {
            logger.info("Request: Update payment status for room id: {} to: {}", id, paymentStatus);
            kamar updatedRoom = roomService.updatePaymentStatus(id, paymentStatus);
            return ResponseEntity.ok(updatedRoom);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating payment status for id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating payment status {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengupdate status pembayaran: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/rooms/{id} - Hapus kamar
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        try {
            logger.info("Request: Delete room with id: {}", id);
            roomService.deleteRoom(id);
            logger.info("Room deleted successfully with id: {}", id);
            return ResponseEntity.ok(Map.of("message", "Kamar berhasil dihapus"));
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting room {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error deleting room {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal menghapus kamar: " + e.getMessage()));
        }
    }

    /**
     * GET /api/rooms/check/{nomorKamar} - Cek apakah nomor kamar sudah ada
     */
    @GetMapping("/check/{nomorKamar}")
    public ResponseEntity<?> checkRoomNumber(@PathVariable String nomorKamar) {
        try {
            logger.info("Request: Check if room number exists: {}", nomorKamar);
            boolean exists = roomService.isRoomNumberExists(nomorKamar);
            Map<String, Object> response = new HashMap<>();
            response.put("nomorKamar", nomorKamar);
            response.put("exists", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error checking room number {}: {}", nomorKamar, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengecek nomor kamar: " + e.getMessage()));
        }
    }
}
