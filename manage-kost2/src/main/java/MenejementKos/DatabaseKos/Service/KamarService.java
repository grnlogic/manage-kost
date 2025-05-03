package MenejementKos.DatabaseKos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MenejementKos.DatabaseKos.repository.KamarRepository;
import MenejementKos.DatabaseKos.model.kamar;

@Service
public class KamarService {

    @Autowired
    private KamarRepository kamarRepository;

    public List<kamar> getAllKamar() {
        return kamarRepository.findAll();
    }

    public Optional<kamar> getKamarById(Long id) {
        return kamarRepository.findById(id);
    }

    public kamar saveKamar(kamar kamar) {
        return kamarRepository.save(kamar);
    }

    public void deleteKamar(Long id) {
        kamarRepository.deleteById(id);
    }

    public boolean nomorKamarExists(String nomorKamar) {
        return kamarRepository.existsByNomorKamar(nomorKamar);
    }
}
