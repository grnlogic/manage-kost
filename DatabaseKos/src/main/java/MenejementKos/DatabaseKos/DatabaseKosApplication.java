package MenejementKos.DatabaseKos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "MenejementKos.DatabaseKos.model")
public class DatabaseKosApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseKosApplication.class, args);
	}

}
