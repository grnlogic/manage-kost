package MenejementKos.DatabaseKos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MenejementKos.DatabaseKos.model.peraturan;
import MenejementKos.DatabaseKos.repository.peraturanRepository;

@Service
public class PeraturanService {
    @Autowired
    private peraturanRepository peraturanRepository;

    public List<peraturan> getAllPeraturan() {
        return peraturanRepository.findAll();
    }

    public Optional<peraturan> getPeraturanById(Long id) {
        return peraturanRepository.findById(id);
    }

    public void deletePeraturanById(Long id) {
        peraturanRepository.deleteById(id);
    }
    public peraturan savePeraturan(peraturan peraturan) {
        return peraturanRepository.save(peraturan);
    }

}
