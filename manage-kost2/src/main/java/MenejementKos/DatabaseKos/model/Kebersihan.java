package MenejementKos.DatabaseKos.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "kebersihan")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Kebersihan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;

    private boolean areaParking;
    private boolean areaCorridor;
    private boolean areaTerrace;
    private boolean areaGarden;

    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Manual getters
    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public boolean isAreaParking() {
        return areaParking;
    }

    public boolean isAreaCorridor() {
        return areaCorridor;
    }

    public boolean isAreaTerrace() {
        return areaTerrace;
    }

    public boolean isAreaGarden() {
        return areaGarden;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Manual setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setAreaParking(boolean areaParking) {
        this.areaParking = areaParking;
    }

    public void setAreaCorridor(boolean areaCorridor) {
        this.areaCorridor = areaCorridor;
    }

    public void setAreaTerrace(boolean areaTerrace) {
        this.areaTerrace = areaTerrace;
    }

    public void setAreaGarden(boolean areaGarden) {
        this.areaGarden = areaGarden;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Kebersihan{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                '}';
    }
}
