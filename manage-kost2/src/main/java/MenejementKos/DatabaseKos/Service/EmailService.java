package MenejementKos.DatabaseKos.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username:noreply@kosapp.com}")
    private String fromEmail;
    
    @Value("${spring.mail.enabled:false}")
    private boolean mailEnabled;

    public void sendEmail(String to, String subject, String body) {
        logger.info("Preparing to send email to {}, subject: {}", to, subject);
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        
        try {
            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email: {}", e.getMessage(), e);
        }
    }
    
    public void sendOtpEmail(String to, String otp) {
        String subject = "KosApp - Kode Verifikasi OTP Anda";
        String body = "Kode verifikasi OTP Anda adalah: " + otp + "\n\n" +
                "Kode ini berlaku selama 10 menit. Jangan bagikan kode ini kepada siapapun.";
        
        sendEmail(to, subject, body);
    }
    
    public boolean isEmailEnabled() {
        return mailEnabled;
    }
}
