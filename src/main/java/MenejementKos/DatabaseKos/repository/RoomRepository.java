package MenejementKos.DatabaseKos.repository;

import MenejementKos.DatabaseKos.model.kamar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<kamar, Long> {
    
    // Cari kamar berdasarkan nomor kamar
    Optional<kamar> findByNomorKamar(String nomorKamar);
    
    // Cek apakah nomor kamar sudah ada
    boolean existsByNomorKamar(String nomorKamar);
    
    // Cari semua kamar berdasarkan status (kosong, terisi, pending)
    List<kamar> findByStatus(String status);
    
    // Cari kamar dengan harga kurang dari atau sama dengan nilai tertentu
    List<kamar> findByHargaBulananLessThanEqual(Double maxPrice);
    
    // Cari kamar dengan harga antara range tertentu
    List<kamar> findByHargaBulananBetween(Double minPrice, Double maxPrice);
    
    // Cari kamar berdasarkan status pembayaran
    List<kamar> findByStatusPembayaran(String statusPembayaran);
    
    // Custom query: Cari kamar yang tersedia (kosong)
    @Query("SELECT k FROM kamar k WHERE k.status = 'kosong' ORDER BY k.hargaBulanan ASC")
    List<kamar> findAvailableRooms();
    
    // Custom query: Hitung jumlah kamar berdasarkan status
    @Query("SELECT COUNT(k) FROM kamar k WHERE k.status = ?1")
    Long countByStatus(String status);
    
    // Custom query: Cari kamar berdasarkan fasilitas (contains)
    @Query("SELECT k FROM kamar k WHERE LOWER(k.fasilitas) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<kamar> findByFasilitasContaining(String facility);
}
