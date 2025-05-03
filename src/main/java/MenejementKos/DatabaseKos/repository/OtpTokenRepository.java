package MenejementKos.DatabaseKos.repository;

import MenejementKos.DatabaseKos.model.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findTopByEmailOrderByExpiredAtDesc(String email);
    
    // Added from duplicate repository
    Optional<OtpToken> findByEmail(String email);
    
    // Added from duplicate repository
    void deleteByEmail(String email);
}
