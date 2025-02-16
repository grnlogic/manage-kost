package springDatabase.DatabaseSpringBoot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j  // Untuk logging dengan Lombok
public class DatabaseSpringBootApplication implements CommandLineRunner {

    private final DataSource dataSource;

    public DatabaseSpringBootApplication(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(DatabaseSpringBootApplication.class, args);
    }

    @Override
    public void run(final String... args) {
        log.info("Datasource: " + dataSource.toString());
        final JdbcTemplate restTemplate = new JdbcTemplate(dataSource);
        restTemplate.execute("SELECT 1");
    }
}
