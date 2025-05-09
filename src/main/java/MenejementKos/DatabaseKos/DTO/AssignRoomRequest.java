package MenejementKos.DatabaseKos.DTO;

public class AssignRoomRequest {
    private Long roomId;
    private Integer durasiSewa;
    private String tanggalMulai;
    private String metodePembayaran;
    
    // Getter dan Setter
    public Long getRoomId() {
        return roomId;
    }
    
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    
    public Integer getDurasiSewa() {
        return durasiSewa;
    }
    
    public void setDurasiSewa(Integer durasiSewa) {
        this.durasiSewa = durasiSewa;
    }
    
    public String getTanggalMulai() {
        return tanggalMulai;
    }
    
    public void setTanggalMulai(String tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }
    
    public String getMetodePembayaran() {
        return metodePembayaran;
    }
    
    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }
}
