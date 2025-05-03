package MenejementKos.DatabaseKos.repository;

import MenejementKos.DatabaseKos.model.kamar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<kamar, Long> {
    // You can add custom query methods here if needed
}
