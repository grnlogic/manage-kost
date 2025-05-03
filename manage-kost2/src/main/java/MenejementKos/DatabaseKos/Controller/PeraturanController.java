package MenejementKos.DatabaseKos.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import MenejementKos.DatabaseKos.Service.PeraturanService;
import MenejementKos.DatabaseKos.model.peraturan;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/peraturan")
public class PeraturanController {
    @Autowired
    private PeraturanService peraturanService;

    @GetMapping
    public List<peraturan> getAllPeraturan() {
        System.out.println("Fetching all peraturan...");
        return peraturanService.getAllPeraturan();
    }

    @GetMapping("/{id}")
    public peraturan getPeraturanById(@PathVariable Long id) {
        return peraturanService.getPeraturanById(id).orElse(null);
    }

    @PostMapping
    public peraturan addPeraturan(@RequestBody peraturan peraturan) {
        System.out.println("Received peraturan: " + peraturan.getJudul_peraturan() + ", " + peraturan.getDeskripsi_peraturan());
        return peraturanService.savePeraturan(peraturan);
    }

    @PutMapping("/{id}")
    public peraturan updatePeraturan(@PathVariable Long id, @RequestBody peraturan peraturan) {
        peraturan.setId(id);
        return peraturanService.savePeraturan(peraturan);
    }

    @DeleteMapping("/{id}")
    public void deletePeraturan(@PathVariable Long id) {
        peraturanService.deletePeraturanById(id);
    }
}
