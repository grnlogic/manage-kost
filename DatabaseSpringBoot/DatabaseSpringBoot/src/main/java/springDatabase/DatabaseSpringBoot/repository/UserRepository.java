package springDatabase.DatabaseSpringBoot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springDatabase.DatabaseSpringBoot.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
