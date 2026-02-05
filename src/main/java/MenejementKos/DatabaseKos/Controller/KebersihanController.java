package MenejementKos.DatabaseKos.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MenejementKos.DatabaseKos.Service.KebersihanService;
import MenejementKos.DatabaseKos.model.Kebersihan;

@RestController
@RequestMapping("/api/kebersihan")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KebersihanController {
     private final KebersihanService service;
     private final Logger logger = LoggerFactory.getLogger(KebersihanController.class);

    public KebersihanController(KebersihanService service) {
        this.service = service;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        logger.info("Test endpoint called");
        return ResponseEntity.ok("API berfungsi! Server merespons dengan baik.");
    }

    @GetMapping
    public ResponseEntity<List<Kebersihan>> getAll() {
        logger.info("GET request received for /api/kebersihan");
        try {
            List<Kebersihan> result = service.getAll();
            logger.info("Retrieved {} records from service", result.size());
            
            if (result.isEmpty()) {
                logger.info("Tidak ada data kebersihan yang ditemukan");
                return ResponseEntity.ok().body(result);
            }
            
            // Log raw data untuk debugging
            for (Kebersihan k : result) {
                logger.info("Data: {}", k);
            }
            
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            logger.error("Error retrieving kebersihan data: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public Kebersihan getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Kebersihan create(@RequestBody Kebersihan kebersihan) {
        return service.create(kebersihan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kebersihan> update(@PathVariable Long id, @RequestBody Kebersihan kebersihan) {
        try {
            logger.info("Creating new kebersihan record (overwrite) with reference id: {} and data: {}", id, kebersihan);
            Kebersihan created = service.update(id, kebersihan);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.error("Error creating new kebersihan record: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
