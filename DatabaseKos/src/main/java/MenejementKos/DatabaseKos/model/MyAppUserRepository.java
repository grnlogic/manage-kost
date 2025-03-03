package MenejementKos.DatabaseKos.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyAppUserRepository extends JpaRepository<MyAppUser, Long> {
    Optional<MyAppUser> findByUsername(String username);
    Optional<MyAppUser> findByEmail(String email);
}
