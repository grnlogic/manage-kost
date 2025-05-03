package MenejementKos.DatabaseKos.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MenejementKos.DatabaseKos.model.Kebersihan;
import MenejementKos.DatabaseKos.repository.KebersihanRepository;

@Service
public class KebersihanService {
      private final KebersihanRepository repository;
      private final Logger logger = LoggerFactory.getLogger(KebersihanService.class);

    public KebersihanService(KebersihanRepository repository) {
        this.repository = repository;
    }

    public List<Kebersihan> getAll() {
        logger.info("Fetching all kebersihan records from repository");
        List<Kebersihan> result = repository.findAll();
        logger.info("Found {} kebersihan records", result.size());
        return result;
    }

    public Kebersihan getById(Long id) {
        logger.info("Fetching kebersihan record with id {}", id);
        Kebersihan result = repository.findById(id).orElseThrow(() -> new RuntimeException("Data tidak ditemukan"));
        logger.info("Found kebersihan record: {}", result);
        return result;
    }

    public Kebersihan create(Kebersihan kebersihan) {
        logger.info("Creating new kebersihan record: {}", kebersihan);
        Kebersihan result = repository.save(kebersihan);
        logger.info("Created kebersihan record: {}", result);
        return result;
    }

    public Kebersihan update(Long id, Kebersihan updated) {
        logger.info("Updating kebersihan record with id {}", id);
        
        try {
            Kebersihan existing = getById(id);

            // Only update fields that are present in the request
            if (updated.getRoomNumber() != null) {
                existing.setRoomNumber(updated.getRoomNumber());
            }
            existing.setAreaParking(updated.isAreaParking());
            existing.setAreaCorridor(updated.isAreaCorridor());
            existing.setAreaTerrace(updated.isAreaTerrace());
            existing.setAreaGarden(updated.isAreaGarden());
            
            
            
            if (updated.getNotes() != null) {
                existing.setNotes(updated.getNotes());
            }

            Kebersihan result = repository.save(existing);
            logger.info("Updated kebersihan record: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Error updating kebersihan record with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public void delete(Long id) {
        logger.info("Deleting kebersihan record with id {}", id);
        repository.deleteById(id);
        logger.info("Deleted kebersihan record with id {}", id);
    }
}
