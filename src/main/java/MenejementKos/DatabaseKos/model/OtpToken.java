package MenejementKos.DatabaseKos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_tokens")
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String otp;
    
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    public OtpToken() {}

    public OtpToken(String email, String otp, LocalDateTime expiredAt) {
        this.email = email;
        this.otp = otp;
        this.expiredAt = expiredAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
}
