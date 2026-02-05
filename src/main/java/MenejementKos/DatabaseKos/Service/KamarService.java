package MenejementKos.DatabaseKos.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MenejementKos.DatabaseKos.repository.KamarRepository;
import MenejementKos.DatabaseKos.model.kamar;

@Service
public class KamarService {

    private static final Logger logger = LoggerFactory.getLogger(KamarService.class);

    @Autowired
    private KamarRepository kamarRepository;

    public List<kamar> getAllKamar() {
        return kamarRepository.findAll();
    }

    public Optional<kamar> getKamarById(Long id) {
        return kamarRepository.findById(id);
    }

    public kamar saveKamar(kamar kamar) {
        // Validasi dan set default values
        if (kamar.getNomorKamar() == null || kamar.getNomorKamar().trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor kamar tidak boleh kosong");
        }
        
        if (kamar.getHargaBulanan() <= 0) {
            throw new IllegalArgumentException("Harga bulanan harus lebih dari 0");
        }
        
        // Set default values jika null
        if (kamar.getStatus() == null || kamar.getStatus().trim().isEmpty()) {
            kamar.setStatus("kosong");
        }
        
        if (kamar.getFasilitas() == null) {
            kamar.setFasilitas("");
        }
        
        // Set title default jika kosong
        if (kamar.getTitle() == null || kamar.getTitle().trim().isEmpty()) {
            kamar.setTitle("Kamar " + kamar.getNomorKamar());
        }
        
        // Set description default jika kosong
        if (kamar.getDescription() == null || kamar.getDescription().trim().isEmpty()) {
            kamar.setDescription("Kamar kost nomor " + kamar.getNomorKamar());
        }
        
        // Set price sama dengan hargaBulanan jika tidak diisi
        if (kamar.getPrice() == null || kamar.getPrice() <= 0) {
            kamar.setPrice(kamar.getHargaBulanan());
        }
        
        // Check duplicate nomor kamar untuk create baru
        if (kamar.getId() == null && kamarRepository.existsByNomorKamar(kamar.getNomorKamar())) {
            throw new IllegalArgumentException("Nomor kamar " + kamar.getNomorKamar() + " sudah ada");
        }
        
        logger.info("Saving kamar: {}", kamar.getNomorKamar());
        return kamarRepository.save(kamar);
    }

    public void deleteKamar(Long id) {
        kamarRepository.deleteById(id);
    }

    public boolean nomorKamarExists(String nomorKamar) {
        return kamarRepository.existsByNomorKamar(nomorKamar);
    }
}
