package MenejementKos.DatabaseKos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(MailConfig.class);
    
    @Value("${spring.mail.host:}")
    private String host;
    
    @Value("${spring.mail.port:0}")
    private int port;
    
    @Value("${spring.mail.username:}")
    private String username;
    
    @Value("${spring.mail.password:}")
    private String password;

    /**
     * Creates a real JavaMailSender when mail is enabled
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "spring.mail.enabled", havingValue = "true")
    public JavaMailSender realMailSender() {
        logger.info("Configuring real mail sender with host: {}, port: {}", host, port);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        return mailSender;
    }
    
    /**
     * Creates a dummy JavaMailSender when mail is disabled
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "spring.mail.enabled", havingValue = "false", matchIfMissing = true)
    public JavaMailSender dummyMailSender() {
        logger.info("Email is disabled. Using dummy mail sender that logs instead of sending emails.");
        return new DummyMailSender();
    }
    
    /**
     * Simple implementation that doesn't actually send emails but logs them
     */
    private static class DummyMailSender extends JavaMailSenderImpl {
        private final Logger logger = LoggerFactory.getLogger(DummyMailSender.class);
        
        @Override
        public void send(org.springframework.mail.SimpleMailMessage simpleMessage) {
            logger.info("DUMMY EMAIL: Would have sent email to: {} with subject: {}", 
                    String.join(", ", simpleMessage.getTo()), 
                    simpleMessage.getSubject());
        }
        
        @Override
        public void send(org.springframework.mail.SimpleMailMessage... simpleMessages) {
            for (org.springframework.mail.SimpleMailMessage message : simpleMessages) {
                send(message);
            }
        }
        
        @Override
        public void send(org.springframework.mail.javamail.MimeMessagePreparator mimeMessagePreparator) {
            logger.info("DUMMY EMAIL: Would have sent MIME message");
        }
        
        @Override
        public void send(org.springframework.mail.javamail.MimeMessagePreparator... mimeMessagePreparators) {
            logger.info("DUMMY EMAIL: Would have sent {} MIME messages", mimeMessagePreparators.length);
        }
    }
}
