package MenejementKos.DatabaseKos.Controller;

import MenejementKos.DatabaseKos.DTO.RegisterRequest;
import MenejementKos.DatabaseKos.Service.UserService;
import MenejementKos.DatabaseKos.DTO.AssignRoomRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://kos-app-frontend-rzng-beta.vercel.app",
    "https://kos-app-frontend.vercel.app"
}) 
public class ContentController {
    
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        System.out.println("GET /users endpoint called");
        return userService.getAllUsers();
    }

    // Mengubah endpoint dari /assign-room menjadi /assign-user-room untuk konsistensi penamaan
    @PutMapping("/{userId}/assign-user-room")
    public ResponseEntity<?> assignRoom(
        @PathVariable Long userId,
        @RequestBody AssignRoomRequest request
    ) {
        return userService.assignRoom(userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
        @PathVariable Long userId,
        @RequestBody RegisterRequest request
    ) {
        return userService.updateUser(userId, request);
    }

    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<?> resetPassword(
        @PathVariable Long userId,
        @RequestBody Map<String, String> request
    ) {
        return userService.resetPassword(userId, request.get("newPassword"));
    }

    // Mengubah nama endpoint dari /some-endpoint menjadi /user-endpoint untuk kejelasan fungsi
    @GetMapping("/user-endpoint")
    public ResponseEntity<?> someEndpoint(@RequestParam(required = false) String id) {
        // Validasi jika ID null atau tidak valid
        if (id == null || id.equalsIgnoreCase("undefined")) {
            return ResponseEntity.badRequest().body("ID tidak valid!");
        }

        try {
            Long validId = Long.parseLong(id); // Konversi String ke Long
            // ...proses dengan validId...
            return ResponseEntity.ok("Proses berhasil dengan ID: " + validId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("ID harus berupa angka!");
        }
    }
}
