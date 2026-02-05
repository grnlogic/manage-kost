package MenejementKos.DatabaseKos.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import MenejementKos.DatabaseKos.Service.PengumumanService;
import MenejementKos.DatabaseKos.model.PengumumanAdmin;

@CrossOrigin(origins = {
    "http://141.11.25.167:3000",
    "https://kos-app-frontend.vercel.app"
})
@RestController
@RequestMapping("/api/pengumuman")
public class PengumumanController {

    @Autowired
    private PengumumanService pengumumanService;

    @GetMapping
    public List<PengumumanAdmin> getAllPengumuman() {
        return pengumumanService.getAllPengumuman();
    }

    @GetMapping("/{id}")
    public PengumumanAdmin getPengumumanById(@PathVariable Long id) {
        Optional<PengumumanAdmin> pengumuman = (Optional<PengumumanAdmin>) pengumumanService.getPengumumanById(id);
        return pengumuman.orElse(null);
    }

    @PostMapping
    public PengumumanAdmin addPengumuman(@RequestBody PengumumanAdmin pengumuman) {
        return pengumumanService.savePengumuman(pengumuman);
    }

    @PutMapping("/{id}")
    public PengumumanAdmin updatePengumuman(@PathVariable Long id, @RequestBody PengumumanAdmin pengumuman) {
        pengumuman.setId(id);
        return pengumumanService.savePengumuman(pengumuman);
    }

    @DeleteMapping("/{id}")
    public void deletePengumuman(@PathVariable Long id) {
        pengumumanService.deletePengumuman(id);
    }
}
