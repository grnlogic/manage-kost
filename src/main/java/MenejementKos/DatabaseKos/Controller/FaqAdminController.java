package MenejementKos.DatabaseKos.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping; // Perbaiki import yang salah
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import MenejementKos.DatabaseKos.Service.FaqAdminService;
import MenejementKos.DatabaseKos.model.FaqAdmin;

@RestController
@RequestMapping("/api/faqs")
@CrossOrigin(origins = {"http://141.11.25.167:3000", "*"}) // izinkan frontend React localhost
public class FaqAdminController {
    private final FaqAdminService faqAdminService;
    public FaqAdminController(FaqAdminService faqAdminService) {
        this.faqAdminService = faqAdminService;
    }

    @GetMapping
public ResponseEntity<List<Map<String, Object>>> getAllFaqs() {
    List<Map<String, Object>> faqs = faqAdminService.getAllFaqs().stream()
        .map(faq -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", faq.getId()); 
            // Include both naming conventions for compatibility with all components
            map.put("pertanyaan", faq.getPertanyaan());
            map.put("jawaban", faq.getJawaban());
            map.put("question", faq.getPertanyaan());
            map.put("answer", faq.getJawaban());
            return map;
        })
        .toList();
    return ResponseEntity.ok(faqs);
}


    @GetMapping("/{id}")
    public ResponseEntity<FaqAdmin> getFaqById(@PathVariable String id) {
        if (id == null || id.equals("undefined")) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Long parsedId = Long.parseLong(id);
            return faqAdminService.getFaqById(parsedId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public FaqAdmin createFaq(@RequestBody FaqAdmin faq) {
        return faqAdminService.createFaq(faq);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FaqAdmin> updateFaq(@PathVariable Long id, @RequestBody FaqAdmin faq) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(faqAdminService.updateFaq(id, faq));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Long id) {
        if (id == null || id <= 0) { // Validasi tambahan
            return ResponseEntity.badRequest().build();
        }
        faqAdminService.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }
}