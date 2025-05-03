package MenejementKos.DatabaseKos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "kamar")
// @Data
// @AllArgsConstructor

public class kamar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "nomor_kamar", nullable = false, unique = true)
    private String nomorKamar;

    @Column(nullable = false)
    private String status; //kosong, terisi, panding

    @Column(name = "harga_bulan", nullable = false)
    private Double hargaBulanan;

    @Column(columnDefinition = "TEXT")
    private String fasilitas;

    @Column(nullable = false)
    private String title; // Add title field

    @Column(columnDefinition = "TEXT")
    private String description; // Add description field

    @Column(nullable = false)
    private Double price; // Add price field

    @Column(name = "status_pembayaran")
    private String statusPembayaran;

    //Getter setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNomorKamar() {
        return nomorKamar;
    }
    public void setNomorKamar(String nomorKamar) {
        this.nomorKamar = nomorKamar;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public double getHargaBulanan() {
        return hargaBulanan;
    }
    public void setHargaBulanan(double hargaBulanan) {
        this.hargaBulanan = hargaBulanan;
    }
    public String getFasilitas() {
        return fasilitas;
    }
    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public void setStatusPembayaran(String statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }
}
