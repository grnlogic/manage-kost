package MenejementKos.DatabaseKos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "pengumuman")
public class PengumumanAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String judul;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String isi;

    @Column(name = "tanggal_berlaku", nullable = false)
    private LocalDate tanggalBerlaku; // Tambahan untuk tanggal berlaku

    public void setId(Long id) {
        this.id = id;
    }
}
