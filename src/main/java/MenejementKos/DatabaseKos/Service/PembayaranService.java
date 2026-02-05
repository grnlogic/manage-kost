package MenejementKos.DatabaseKos.Service;

import MenejementKos.DatabaseKos.model.Pembayaran;
import MenejementKos.DatabaseKos.model.kamar;
import MenejementKos.DatabaseKos.model.MyAppUser;
import MenejementKos.DatabaseKos.repository.PembayaranRepository;
import MenejementKos.DatabaseKos.repository.KamarRepository;
import MenejementKos.DatabaseKos.model.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PembayaranService {

    @Autowired
    private PembayaranRepository pembayaranRepository;

    @Autowired
    private KamarRepository kamarRepository;

    @Autowired
    private MyAppUserRepository userRepository;

    public List<Pembayaran> getAllPembayaran() {
        return pembayaranRepository.findAll();
    }

    public Optional<Pembayaran> getPembayaranById(Long id) {
        return pembayaranRepository.findById(id);
    }

    public List<Pembayaran> getPembayaranByKamarId(Long kamarId) {
        return pembayaranRepository.findByKamarId(kamarId);
    }

    public List<Pembayaran> getPembayaranByUserId(Long userId) {
        return pembayaranRepository.findByUserId(userId);
    }

    public List<Pembayaran> getPembayaranByStatus(String status) {
        return pembayaranRepository.findByStatus(status);
    }

    public Pembayaran createPembayaran(Pembayaran pembayaran) {
        // Auto-populate data from kamar if kamarId is provided
        if (pembayaran.getKamarId() != null) {
            Optional<kamar> kamarOpt = kamarRepository.findById(pembayaran.getKamarId());
            if (kamarOpt.isPresent()) {
                kamar room = kamarOpt.get();
                pembayaran.setKamar(room.getNomorKamar());
                pembayaran.setNominal(room.getHargaBulanan());
                
                // Find user associated with this room
                Optional<MyAppUser> userOpt = userRepository.findByRoomId(pembayaran.getKamarId());
                if (userOpt.isPresent()) {
                    MyAppUser user = userOpt.get();
                    pembayaran.setUserId(user.getId());
                    pembayaran.setPenghuni(user.getUsername());
                }
            }
        }
        
        return pembayaranRepository.save(pembayaran);
    }

    public Pembayaran updatePembayaran(Long id, Pembayaran pembayaranDetails) {
        Optional<Pembayaran> pembayaranOpt = pembayaranRepository.findById(id);
        if (pembayaranOpt.isPresent()) {
            Pembayaran pembayaran = pembayaranOpt.get();
            
            if (pembayaranDetails.getStatus() != null) {
                pembayaran.setStatus(pembayaranDetails.getStatus());
            }
            if (pembayaranDetails.getTanggalBayar() != null) {
                pembayaran.setTanggalBayar(pembayaranDetails.getTanggalBayar());
            }
            if (pembayaranDetails.getCatatan() != null) {
                pembayaran.setCatatan(pembayaranDetails.getCatatan());
            }
            if (pembayaranDetails.getNominal() != null) {
                pembayaran.setNominal(pembayaranDetails.getNominal());
            }
            
            return pembayaranRepository.save(pembayaran);
        }
        throw new IllegalArgumentException("Pembayaran dengan id " + id + " tidak ditemukan");
    }

    public Pembayaran updateStatusPembayaran(Long id, String status) {
        Optional<Pembayaran> pembayaranOpt = pembayaranRepository.findById(id);
        if (pembayaranOpt.isPresent()) {
            Pembayaran pembayaran = pembayaranOpt.get();
            pembayaran.setStatus(status);
            
            // If status is "Lunas", set tanggalBayar
            if ("Lunas".equals(status) && pembayaran.getTanggalBayar() == null) {
                pembayaran.setTanggalBayar(LocalDateTime.now());
            }
            
            // Update kamar status pembayaran as well
            if (pembayaran.getKamarId() != null) {
                Optional<kamar> kamarOpt = kamarRepository.findById(pembayaran.getKamarId());
                if (kamarOpt.isPresent()) {
                    kamar room = kamarOpt.get();
                    room.setStatusPembayaran(status);
                    kamarRepository.save(room);
                }
            }
            
            return pembayaranRepository.save(pembayaran);
        }
        throw new IllegalArgumentException("Pembayaran dengan id " + id + " tidak ditemukan");
    }

    public void deletePembayaran(Long id) {
        pembayaranRepository.deleteById(id);
    }

    public void generatePembayaranBulanan() {
        // Get all occupied rooms
        List<kamar> occupiedRooms = kamarRepository.findByStatus("terisi");
        
        LocalDateTime now = LocalDateTime.now();
        String bulan = now.getMonth().toString();
        int tahun = now.getYear();
        
        for (kamar room : occupiedRooms) {
            // Check if payment for this month already exists
            List<Pembayaran> existingPayments = pembayaranRepository.findByKamarId(room.getId());
            boolean paymentExists = existingPayments.stream()
                .anyMatch(p -> bulan.equals(p.getBulanBayar()) && tahun == p.getTahunBayar());
            
            if (!paymentExists) {
                Pembayaran pembayaran = new Pembayaran();
                pembayaran.setKamarId(room.getId());
                pembayaran.setKamar(room.getNomorKamar());
                pembayaran.setNominal(room.getHargaBulanan());
                pembayaran.setStatus("Belum Bayar");
                pembayaran.setBulanBayar(bulan);
                pembayaran.setTahunBayar(tahun);
                pembayaran.setTanggalJatuhTempo(now.plusDays(30)); // Due in 30 days
                
                // Find user associated with this room
                Optional<MyAppUser> userOpt = userRepository.findByRoomId(room.getId());
                if (userOpt.isPresent()) {
                    MyAppUser user = userOpt.get();
                    pembayaran.setUserId(user.getId());
                    pembayaran.setPenghuni(user.getUsername());
                }
                
                pembayaranRepository.save(pembayaran);
            }
        }
    }
}
