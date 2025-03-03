package MenejementKos.DatabaseKos.Service;

import MenejementKos.DatabaseKos.DTO.LoginRequest;
import MenejementKos.DatabaseKos.DTO.RegisterRequest;
import MenejementKos.DatabaseKos.model.MyAppUser;
import MenejementKos.DatabaseKos.model.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Collections;


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

        userRepository.save(newUser);
        return ResponseEntity.ok("Registrasi berhasil!");
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        Optional<MyAppUser> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Username tidak ditemukan!");
        }
    
        MyAppUser user = userOptional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Password salah!");
        }
    
        // Dummy token (di real case, gunakan JWT)
        String token = "dummy-token-" + user.getUsername();  
    
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
    
}
