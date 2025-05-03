package MenejementKos.DatabaseKos.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    
    private final Logger logger = LoggerFactory.getLogger(OtpService.class);
    
    // In-memory storage for OTPs (email -> {otp, expiry time})
    private final Map<String, Map<String, Object>> otpStorage = new ConcurrentHashMap<>();
    
    @Autowired
    private JavaMailSender mailSender;
    
    // OTP validity period in minutes
    private static final int OTP_VALIDITY_MINUTES = 10;
    
    /**
     * Generate and send OTP to the user's email
     * @param email User's email
     * @return generated OTP
     */
    public String generateAndSendOtp(String email) {
        String otp = generateOtp();
        
        // Store OTP with expiry time
        Map<String, Object> otpData = new ConcurrentHashMap<>();
        otpData.put("otp", otp);
        otpData.put("expiryTime", LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
        
        otpStorage.put(email, otpData);
        
        // Send email with OTP
        try {
            sendOtpEmail(email, otp);
            logger.info("OTP email sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send OTP email to {}: {}", email, e.getMessage());
        }
        
        return otp;
    }
    
    /**
     * Verify if the provided OTP is valid for the email
     * @param email User's email
     * @param otp OTP to verify
     * @return true if valid, false otherwise
     */
    public boolean verifyOtp(String email, String otp) {
        Map<String, Object> otpData = otpStorage.get(email);
        
        if (otpData == null) {
            logger.warn("No OTP found for email: {}", email);
            return false;
        }
        
        String storedOtp = (String) otpData.get("otp");
        LocalDateTime expiryTime = (LocalDateTime) otpData.get("expiryTime");
        
        if (LocalDateTime.now().isAfter(expiryTime)) {
            logger.warn("OTP expired for email: {}", email);
            otpStorage.remove(email);
            return false;
        }
        
        if (storedOtp.equals(otp)) {
            // OTP is used, remove it
            otpStorage.remove(email);
            logger.info("OTP verified successfully for email: {}", email);
            return true;
        }
        
        logger.warn("Invalid OTP provided for email: {}", email);
        return false;
    }
    
    /**
     * Remove any stored OTP for the email
     * @param email User's email
     */
    public void clearOtp(String email) {
        otpStorage.remove(email);
    }
    
    // Generate a random 6-digit OTP
    private String generateOtp() {
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000);
        return String.valueOf(otpNumber);
    }
    
    // Send email with OTP
    private void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verifikasi OTP untuk Pendaftaran Akun Kos App");
        message.setText("Kode OTP Anda adalah: " + otp + "\n\nKode ini berlaku selama " 
                + OTP_VALIDITY_MINUTES + " menit. Jangan bagikan kode ini kepada siapapun.");
        
        mailSender.send(message);
    }
}
