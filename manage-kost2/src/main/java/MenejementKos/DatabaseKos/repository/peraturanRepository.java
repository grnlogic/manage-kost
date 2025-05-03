package MenejementKos.DatabaseKos.repository;

import MenejementKos.DatabaseKos.model.peraturan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface peraturanRepository extends JpaRepository<peraturan, Long> {
    // No instance fields or methods here
}
