package admin;

import admin.Model;
import admin.AdminRepository;
import admin.ServiceKamar;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {
    
    @Autowired
    private AdminRepository AdminRepository;


    @Autowired
    private ServiceKamar serviceKamar;

    @GetMapping
    public List<Model> getAllKamar() {
        return serviceKamar.getAllKamar();
    }
    
    @GetMapping("/{id}")
    public Model getKamarById(@RequestParam Long id) {
        return serviceKamar.getKamarById(id).orElse(null);
    }
    @GetMapping
    public Model addKamar(@RequestParam Model kamar) {
        return serviceKamar.addKamar(kamar);
    }
    @PutMapping("/{id}")
    public Model updateKamar(@PathVariable Long id, @RequestBody Model kamarDetails) {
        return serviceKamar.updateKamar(id, kamarDetails);
    }
    @DeleteMapping("/{id}")
    public void deleteKamar(@PathVariable Long id) {
        serviceKamar.deleteKamar(id);
    }

}
