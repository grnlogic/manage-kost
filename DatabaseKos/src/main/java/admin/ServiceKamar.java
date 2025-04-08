package admin;


import admin.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;



@Service
public class ServiceKamar {
    @Autowired
    private AdminRepository AdminRepository;
    

    public List<Model> getAllKamar() {
        return AdminRepository.findAll();
    }

    public Optional<Model> getKamarById(Long id) {
        return AdminRepository.findById(id);
    }
    public Model addKamar(Model kamar) {
        return AdminRepository.save(kamar);
    }
    public Model updateKamar(Long id, Model kamarDetails) {
        return AdminRepository.findById(id).map(kamar -> {
            kamar.setNomor_kamar(kamarDetails.getNomor_kamar());
            kamar.setStatus(kamarDetails.getStatus());
            return AdminRepository.save(kamar);
        }).orElseThrow(() -> new RuntimeException("Kamar tidak ditemukan"));
    }
    public void deleteKamar(Long id) {
        AdminRepository.deleteById(id);
    }
    
}
