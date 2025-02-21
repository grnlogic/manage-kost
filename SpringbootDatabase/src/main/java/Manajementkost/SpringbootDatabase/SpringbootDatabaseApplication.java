package Manajementkost.SpringbootDatabase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import jakarta.persistence.Entity;
@EnableJpaRepositories("Manajementkost.repository")
@SpringBootApplication
@EntityScan(basePackages = "Manajementkost.entity")
@ComponentScan(basePackages = "Manajementkost")
public class SpringbootDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDatabaseApplication.class, args);
	}

}
