package MenejementKos.DatabaseKos.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import MenejementKos.DatabaseKos.Service.KamarService;
import MenejementKos.DatabaseKos.model.kamar;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://141.11.25.167:3000") // izinkan frontend React localhost
@RestController
@RequestMapping("/api/kamar")
public class KamarController {

    private static final Logger logger = LoggerFactory.getLogger(KamarController.class);

    @Autowired
    private KamarService kamarService;

    /**
     * GET /api/kamar - Mendapatkan semua kamar
     */
    @GetMapping
    public ResponseEntity<?> getAllKamar() {
        try {
            logger.info("Request: Get all kamar");
            List<kamar> kamars = kamarService.getAllKamar();
            return ResponseEntity.ok(kamars);
        } catch (Exception e) {
            logger.error("Error getting all kamar: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data kamar: " + e.getMessage()));
        }
    }

    /**
     * GET /api/kamar/{id} - Mendapatkan kamar berdasarkan ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getKamarById(@PathVariable Long id) {
        try {
            logger.info("Request: Get kamar by id: {}", id);
            kamar foundKamar = kamarService.getKamarById(id).orElse(null);
            if (foundKamar != null) {
                return ResponseEntity.ok(foundKamar);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Kamar dengan ID " + id + " tidak ditemukan"));
        } catch (Exception e) {
            logger.error("Error getting kamar by id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data kamar: " + e.getMessage()));
        }
    }

    /**
     * POST /api/kamar - Membuat kamar baru
     */
    @PostMapping
    public ResponseEntity<?> addKamar(@RequestBody kamar kamar) {
        try {
            logger.info("Request: Create new kamar with nomor kamar: {}", kamar.getNomorKamar());
            
            // Validasi input
            if (kamar.getNomorKamar() == null || kamar.getNomorKamar().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Nomor kamar tidak boleh kosong"));
            }
            if (kamar.getHargaBulanan() <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Harga bulanan harus lebih dari 0"));
            }
            
            kamar savedKamar = kamarService.saveKamar(kamar);
            logger.info("Kamar created successfully with id: {}", savedKamar.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKamar);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error creating kamar: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating kamar: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal membuat kamar: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/kamar/{id} - Update kamar
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateKamar(@PathVariable Long id, @RequestBody kamar kamar) {
        try {
            logger.info("Request: Update kamar with id: {}", id);
            
            // Cek apakah kamar ada
            if (!kamarService.getKamarById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Kamar dengan ID " + id + " tidak ditemukan"));
            }
            
            kamar.setId(id); // pastikan update berdasarkan ID
            kamar updatedKamar = kamarService.saveKamar(kamar);
            logger.info("Kamar updated successfully with id: {}", id);
            return ResponseEntity.ok(updatedKamar);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error updating kamar {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating kamar {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengupdate kamar: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/kamar/{id} - Hapus kamar
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKamar(@PathVariable Long id) {
        try {
            logger.info("Request: Delete kamar with id: {}", id);
            
            // Cek apakah kamar ada
            if (!kamarService.getKamarById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Kamar dengan ID " + id + " tidak ditemukan"));
            }
            
            kamarService.deleteKamar(id);
            logger.info("Kamar deleted successfully with id: {}", id);
            return ResponseEntity.ok(Map.of("message", "Kamar berhasil dihapus"));
        } catch (Exception e) {
            logger.error("Error deleting kamar {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal menghapus kamar: " + e.getMessage()));
        }
    }
}
