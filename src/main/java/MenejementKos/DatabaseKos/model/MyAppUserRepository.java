package MenejementKos.DatabaseKos.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyAppUserRepository extends JpaRepository<MyAppUser, Long> {
    Optional<MyAppUser> findByUsername(String username);
    
    Optional<MyAppUser> findByEmail(String email);
    
    List<MyAppUser> findByRoomRequestStatusAndRoomIdNotNull(String status);

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
