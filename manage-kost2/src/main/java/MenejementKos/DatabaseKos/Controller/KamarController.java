package MenejementKos.DatabaseKos.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import MenejementKos.DatabaseKos.Service.KamarService;
import MenejementKos.DatabaseKos.model.kamar;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // izinkan frontend React localhost
@RestController
@RequestMapping("/api/kamar")
public class KamarController {

    @Autowired
    private KamarService kamarService;

    @GetMapping
    public List<kamar> getAllKamar() {
        return kamarService.getAllKamar(); // New fields will automatically be included
    }

    @GetMapping("/{id}")
    public kamar getKamarById(@PathVariable Long id) {
        return kamarService.getKamarById(id).orElse(null);
    }

    @PostMapping
    public kamar addKamar(@RequestBody kamar kamar) {
        return kamarService.saveKamar(kamar);
    }

    @PutMapping("/{id}")
    public kamar updateKamar(@PathVariable Long id, @RequestBody kamar kamar) {
        kamar.setId(id); // pastikan update berdasarkan ID
        return kamarService.saveKamar(kamar);
    }

    @DeleteMapping("/{id}")
    public void deleteKamar(@PathVariable Long id) {
        kamarService.deleteKamar(id);
    }
}
