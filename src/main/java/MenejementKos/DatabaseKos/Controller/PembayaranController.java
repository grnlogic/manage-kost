package MenejementKos.DatabaseKos.Controller;

import MenejementKos.DatabaseKos.Service.PembayaranService;
import MenejementKos.DatabaseKos.model.Pembayaran;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pembayaran")
@CrossOrigin(origins = {
    "http://141.11.25.167:3000",
    "https://kos-app-frontend-rzng-beta.vercel.app",
    "https://vercel.com/geran357s-projects/kos-app-frontend-rzng/HLVtQC4FU14UkwetGQG8xkToB97P",
    "https://backend-kos-app.up.railway.app",
    "manage-kost-production.up.railway.app"
})
public class PembayaranController {

    private static final Logger logger = LoggerFactory.getLogger(PembayaranController.class);

    @Autowired
    private PembayaranService pembayaranService;

    /**
     * GET /api/pembayaran - Get all pembayaran
     */
    @GetMapping
    public ResponseEntity<?> getAllPembayaran() {
        try {
            logger.info("Request: Get all pembayaran");
            List<Pembayaran> pembayaranList = pembayaranService.getAllPembayaran();
            logger.info("Response: Found {} pembayaran records", pembayaranList.size());
            return ResponseEntity.ok(pembayaranList);
        } catch (Exception e) {
            logger.error("Error getting all pembayaran: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data pembayaran: " + e.getMessage()));
        }
    }

    /**
     * GET /api/pembayaran/{id} - Get pembayaran by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPembayaranById(@PathVariable Long id) {
        try {
            logger.info("Request: Get pembayaran by id: {}", id);
            Optional<Pembayaran> pembayaranOpt = pembayaranService.getPembayaranById(id);
            if (pembayaranOpt.isPresent()) {
                logger.info("Response: Found pembayaran with id: {}", id);
                return ResponseEntity.ok(pembayaranOpt.get());
            } else {
                logger.warn("Pembayaran with id {} not found", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Pembayaran tidak ditemukan"));
            }
        } catch (Exception e) {
            logger.error("Error getting pembayaran by id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data pembayaran: " + e.getMessage()));
        }
    }

    /**
     * GET /api/pembayaran/kamar/{kamarId} - Get pembayaran by kamar id
     */
    @GetMapping("/kamar/{kamarId}")
    public ResponseEntity<?> getPembayaranByKamarId(@PathVariable Long kamarId) {
        try {
            logger.info("Request: Get pembayaran by kamar id: {}", kamarId);
            List<Pembayaran> pembayaranList = pembayaranService.getPembayaranByKamarId(kamarId);
            logger.info("Response: Found {} pembayaran records for kamar id: {}", pembayaranList.size(), kamarId);
            return ResponseEntity.ok(pembayaranList);
        } catch (Exception e) {
            logger.error("Error getting pembayaran by kamar id {}: {}", kamarId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data pembayaran: " + e.getMessage()));
        }
    }

    /**
     * GET /api/pembayaran/user/{userId} - Get pembayaran by user id
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPembayaranByUserId(@PathVariable Long userId) {
        try {
            logger.info("Request: Get pembayaran by user id: {}", userId);
            List<Pembayaran> pembayaranList = pembayaranService.getPembayaranByUserId(userId);
            logger.info("Response: Found {} pembayaran records for user id: {}", pembayaranList.size(), userId);
            return ResponseEntity.ok(pembayaranList);
        } catch (Exception e) {
            logger.error("Error getting pembayaran by user id {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data pembayaran: " + e.getMessage()));
        }
    }

    /**
     * GET /api/pembayaran/status/{status} - Get pembayaran by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getPembayaranByStatus(@PathVariable String status) {
        try {
            logger.info("Request: Get pembayaran by status: {}", status);
            List<Pembayaran> pembayaranList = pembayaranService.getPembayaranByStatus(status);
            logger.info("Response: Found {} pembayaran records with status: {}", pembayaranList.size(), status);
            return ResponseEntity.ok(pembayaranList);
        } catch (Exception e) {
            logger.error("Error getting pembayaran by status {}: {}", status, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengambil data pembayaran: " + e.getMessage()));
        }
    }

    /**
     * POST /api/pembayaran - Create new pembayaran
     */
    @PostMapping
    public ResponseEntity<?> createPembayaran(@RequestBody Pembayaran pembayaran) {
        try {
            logger.info("Request: Create new pembayaran for kamar id: {}", pembayaran.getKamarId());
            Pembayaran savedPembayaran = pembayaranService.createPembayaran(pembayaran);
            logger.info("Response: Created pembayaran with id: {}", savedPembayaran.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPembayaran);
        } catch (Exception e) {
            logger.error("Error creating pembayaran: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal membuat pembayaran: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/pembayaran/{id} - Update pembayaran
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePembayaran(@PathVariable Long id, @RequestBody Pembayaran pembayaran) {
        try {
            logger.info("Request: Update pembayaran id: {}", id);
            Pembayaran updatedPembayaran = pembayaranService.updatePembayaran(id, pembayaran);
            logger.info("Response: Updated pembayaran with id: {}", id);
            return ResponseEntity.ok(updatedPembayaran);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating pembayaran id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating pembayaran id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengupdate pembayaran: " + e.getMessage()));
        }
    }

    /**
     * PATCH /api/pembayaran/{id}/status - Update status pembayaran
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatusPembayaran(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            logger.info("Request: Update status pembayaran id: {} to: {}", id, status);
            Pembayaran updatedPembayaran = pembayaranService.updateStatusPembayaran(id, status);
            logger.info("Response: Updated status pembayaran with id: {}", id);
            return ResponseEntity.ok(updatedPembayaran);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating status pembayaran id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating status pembayaran id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal mengupdate status pembayaran: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/pembayaran/{id} - Delete pembayaran
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePembayaran(@PathVariable Long id) {
        try {
            logger.info("Request: Delete pembayaran id: {}", id);
            pembayaranService.deletePembayaran(id);
            logger.info("Response: Deleted pembayaran with id: {}", id);
            return ResponseEntity.ok(Map.of("message", "Pembayaran berhasil dihapus"));
        } catch (Exception e) {
            logger.error("Error deleting pembayaran id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal menghapus pembayaran: " + e.getMessage()));
        }
    }

    /**
     * POST /api/pembayaran/generate-monthly - Generate pembayaran bulanan untuk semua kamar terisi
     */
    @PostMapping("/generate-monthly")
    public ResponseEntity<?> generatePembayaranBulanan() {
        try {
            logger.info("Request: Generate pembayaran bulanan");
            pembayaranService.generatePembayaranBulanan();
            logger.info("Response: Pembayaran bulanan berhasil di-generate");
            return ResponseEntity.ok(Map.of("message", "Pembayaran bulanan berhasil di-generate"));
        } catch (Exception e) {
            logger.error("Error generating pembayaran bulanan: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Gagal generate pembayaran bulanan: " + e.getMessage()));
        }
    }
}
