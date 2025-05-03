package MenejementKos.DatabaseKos.repository;

import MenejementKos.DatabaseKos.model.kamar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KamarRepository extends JpaRepository<kamar, Long> {
    boolean existsByNomorKamar(String nomorKamar);
}
