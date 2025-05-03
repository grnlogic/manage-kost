package MenejementKos.DatabaseKos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "MenejementKos.DatabaseKos")
public class DatabaseKosApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(DatabaseKosApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DatabaseKosApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void logApplicationStartup() {
		logger.info("======================================================================");
		logger.info("                      APPLICATION STARTED SUCCESSFULLY                ");
		logger.info("======================================================================");
		logger.info("KosApp is now running! Access the API at /api");
		logger.info("Email functionality is: {}", 
		        System.getProperty("spring.mail.enabled", "false").equals("true") ? "ENABLED" : "DISABLED");
	}

	@PostConstruct
	public void afterStartup() {
	    System.out.println("Application startup complete!");
	}
}
