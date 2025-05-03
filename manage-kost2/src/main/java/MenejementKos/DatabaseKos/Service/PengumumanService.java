package MenejementKos.DatabaseKos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MenejementKos.DatabaseKos.model.PengumumanAdmin;
import MenejementKos.DatabaseKos.repository.PengumumanRepository;
@Service

public class PengumumanService {

    @Autowired
    private PengumumanRepository pengumumanRepository;

    public List<PengumumanAdmin> getAllPengumuman() {
        return pengumumanRepository.findAll();
    }

    public Optional<PengumumanAdmin> getPengumumanById(Long id) {
        return pengumumanRepository.findById(id);
    }

    public PengumumanAdmin savePengumuman(PengumumanAdmin pengumuman) {
        return pengumumanRepository.save(pengumuman);
    }

    public void deletePengumuman(Long id) {
        pengumumanRepository.deleteById(id);
    }
    
}
