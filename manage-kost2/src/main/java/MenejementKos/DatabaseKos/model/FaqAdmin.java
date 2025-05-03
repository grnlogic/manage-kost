package MenejementKos.DatabaseKos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "faq_admin")
public class FaqAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String pertanyaan;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String jawaban;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Temporary manual getter for pertanyaan
    public String getPertanyaan() {
        return pertanyaan;
    }

    // Temporary manual getter for jawaban
    public String getJawaban() {
        return jawaban;
    }

    @PrePersist
    protected void onCreate() {
       createdAt = LocalDateTime.now();
       updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
       updatedAt = LocalDateTime.now();
    }

    public void setPertanyaan(String pertanyaan) {
      this.pertanyaan = pertanyaan;
  }
  
  public void setJawaban(String jawaban) {
      this.jawaban = jawaban;
  }

  public Long getId() {
    return id;
}
}
