package MenejementKos.DatabaseKos.Service;

import MenejementKos.DatabaseKos.model.FaqAdmin;
import MenejementKos.DatabaseKos.repository.FaqAdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class FaqAdminService {
    private final FaqAdminRepository faqRepo;

    public FaqAdminService(FaqAdminRepository faqRepo) {
        this.faqRepo = faqRepo;
        }
    public List<FaqAdmin> getAllFaqs() {
        return faqRepo.findAll();
    }
    public Optional<FaqAdmin> getFaqById(Long id) {
        return faqRepo.findById(id);
    }
    public FaqAdmin saveFaq(FaqAdmin faq) {
        return faqRepo.save(faq);
    }

    public FaqAdmin updateFaq(Long id, FaqAdmin updatedFaq) {
        return faqRepo.findById(id).map(faq -> {
            faq.setPertanyaan(updatedFaq.getPertanyaan());
            faq.setJawaban(updatedFaq.getJawaban());
            return faqRepo.save(faq);
        }).orElseThrow(() -> new RuntimeException("FAQ tidak ditemukan"));
    }
    // Removed duplicate method getAllFaqs()
    
    public void deleteFaq(Long id) {
        faqRepo.deleteById(id);
    }
    public FaqAdmin createFaq(FaqAdmin faq) {
        return faqRepo.save(faq);
    }
}