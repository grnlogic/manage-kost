package MenejementKos.DatabaseKos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "peraturan")
public class peraturan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String judul_peraturan;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String deskripsi_peraturan;

    // Hapus implementasi manual getter dan setter
// Getter and Setter for id
public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getJudul_peraturan() {
    return judul_peraturan;
}

// Setter for judul_peraturan
public void setJudul_peraturan(String judul_peraturan) {
    this.judul_peraturan = judul_peraturan;
}

public String getDeskripsi_peraturan() {
    return deskripsi_peraturan;
}
// Setter for deskripsi_peraturan
public void setDeskripsi_peraturan(String deskripsi_peraturan) {
    this.deskripsi_peraturan = deskripsi_peraturan;
}
    // Lombok akan otomatis generate

}