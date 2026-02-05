package MenejementKos.DatabaseKos.repository;

import MenejementKos.DatabaseKos.model.kamar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KamarRepository extends JpaRepository<kamar, Long> {
    boolean existsByNomorKamar(String nomorKamar);
    List<kamar> findByStatus(String status);
}
