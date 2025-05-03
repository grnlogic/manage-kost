package MenejementKos.DatabaseKos.Controller;

import MenejementKos.DatabaseKos.Service.RoomService;
import MenejementKos.DatabaseKos.model.kamar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = {
    "http://localhost:3000", 
                            "https://kos-app-frontend-rzng-beta.vercel.app", // Removed trailing slash 
                            "https://vercel.com/geran357s-projects/kos-app-frontend-rzng/HLVtQC4FU14UkwetGQG8xkToB97P",
                            "https://backend-kos-app.up.railway.app",
                            "manage-kost-production.up.railway.app"
})
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public List<kamar> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        kamar room = roomService.getRoomById(id);
        if (room != null) {
            return ResponseEntity.ok(room);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody kamar room) {
        kamar savedRoom = roomService.saveRoom(room);
        return ResponseEntity.ok(savedRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody kamar room) {
        room.setId(id);
        kamar updatedRoom = roomService.saveRoom(room);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }
}
