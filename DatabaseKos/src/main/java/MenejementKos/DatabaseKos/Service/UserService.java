package MenejementKos.DatabaseKos.Service;

import MenejementKos.DatabaseKos.DTO.AssignRoomRequest;
import MenejementKos.DatabaseKos.DTO.LoginRequest;
import MenejementKos.DatabaseKos.DTO.RegisterRequest;
import MenejementKos.DatabaseKos.model.MyAppUser;
import MenejementKos.DatabaseKos.model.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private MyAppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username sudah digunakan!");
        }
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email sudah digunakan!");
        }
    
        MyAppUser newUser = new MyAppUser();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Hash password
        newUser.setPhoneNumber(registerRequest.getPhoneNumber());
        newUser.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : "USER"); // Default role USER
    
        userRepository.save(newUser);
        return ResponseEntity.ok("Registrasi berhasil!");
    }
    
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        Optional<MyAppUser> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isEmpty()) {
            System.out.println("Username tidak ditemukan: " + loginRequest.getUsername());
            return ResponseEntity.badRequest().body("Username tidak ditemukan!");
        }
    
        MyAppUser user = userOptional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("Password salah untuk username: " + loginRequest.getUsername());
            return ResponseEntity.badRequest().body("Password salah!");
        }
    
        // Dummy token (di real case, gunakan JWT)
        String token = "dummy-token-" + user.getUsername();
    
        //  Perbaiki pengembalian response menggunakan HashMap untuk menangani `null`
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("roomId", user.getRoomId() != null ? user.getRoomId() : "Belum memilih kamar");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> assignRoom(Long userId, AssignRoomRequest request) {
        if (request.getRoomId() == null) {
            return ResponseEntity.badRequest().body("Room ID tidak boleh kosong!");
        }

        Optional<MyAppUser> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Pengguna tidak ditemukan!");
        }
    
        MyAppUser user = userOptional.get();
        user.setRoomId(request.getRoomId()); //  Simpan roomId di database
        userRepository.save(user);
    
        // Kembalikan data user terbaru, termasuk roomId yang baru
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Room ID berhasil diperbarui!");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("roomId", user.getRoomId());
    
        return ResponseEntity.ok(response);
    }
    
}
