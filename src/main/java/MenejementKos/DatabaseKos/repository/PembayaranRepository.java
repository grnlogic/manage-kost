package MenejementKos.DatabaseKos.repository;

import MenejementKos.DatabaseKos.model.Pembayaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PembayaranRepository extends JpaRepository<Pembayaran, Long> {
    List<Pembayaran> findByKamarId(Long kamarId);
    List<Pembayaran> findByUserId(Long userId);
    List<Pembayaran> findByStatus(String status);
    List<Pembayaran> findByKamarIdAndStatus(Long kamarId, String status);
}
