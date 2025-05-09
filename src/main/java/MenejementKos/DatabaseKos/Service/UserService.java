package MenejementKos.DatabaseKos.Service;

import MenejementKos.DatabaseKos.DTO.AssignRoomRequest;
import MenejementKos.DatabaseKos.DTO.LoginRequest;
import MenejementKos.DatabaseKos.DTO.RegisterRequest;
import MenejementKos.DatabaseKos.DTO.VerifyOtpRequest;
import MenejementKos.DatabaseKos.model.MyAppUser;
import MenejementKos.DatabaseKos.model.MyAppUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class UserService {
    // Add logger instance
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private MyAppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private OtpService otpService;
    
    @Autowired
    private JwtService jwtService;

    public ResponseEntity<?> requestRegistrationOtp(String email) {
        try {
            // Check if email already registered
            if (userRepository.existsByEmail(email)) {
                logger.warn("Registration OTP requested for existing email: {}", email);
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email sudah terdaftar. Silakan login."));
            }
            
            // Generate and send OTP
            otpService.generateAndSendOtp(email);
            
            return ResponseEntity.ok(Map.of(
                "message", "Kode OTP telah dikirim ke email Anda. Silakan periksa kotak masuk atau folder spam."
            ));
        } catch (Exception e) {
            logger.error("Failed to process OTP request: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Gagal mengirim OTP: " + e.getMessage()));
        }
    }
    
    public ResponseEntity<?> verifyOtp(VerifyOtpRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();
        
        if (otpService.verifyOtp(email, otp)) {
            // Check if registration data is included in the request
            if (request.getUsername() != null && request.getPassword() != null) {
                // Create user account immediately after OTP verification
                try {
                    // Validate username and email
                    if (userRepository.existsByUsername(request.getUsername())) {
                        return ResponseEntity.badRequest()
                            .body(Map.of("message", "Username sudah digunakan"));
                    }
                    
                    // Create and save user
                    MyAppUser newUser = new MyAppUser();
                    newUser.setUsername(request.getUsername());
                    newUser.setEmail(email);
                    newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password
                    newUser.setPhoneNumber(request.getPhoneNumber());
                    newUser.setRole(request.getRole() != null ? (String) request.getRole() : "USER"); // Default role USER
                    
                    userRepository.save(newUser);
                    
                    // Clear OTP after successful registration
                    otpService.clearOtp(email);
                    
                    return ResponseEntity.ok(Map.of(
                        "message", "Registrasi berhasil. Silakan login.",
                        "username", newUser.getUsername(),
                        "verified", true
                    ));
                } catch (Exception e) {
                    logger.error("Registration failed after OTP verification: {}", e.getMessage(), e);
                    return ResponseEntity.badRequest()
                        .body(Map.of("message", "Registrasi gagal: " + e.getMessage()));
                }
            } else {
                // If no registration data, just return verification token (old behavior)
                String verificationToken = generateVerificationToken(email);
                
                return ResponseEntity.ok(Map.of(
                    "message", "OTP terverifikasi. Silakan lanjutkan pendaftaran.",
                    "token", verificationToken,
                    "verified", true
                ));
            }
        } else {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "message", "Kode OTP tidak valid atau sudah kedaluwarsa.", 
                    "verified", false
                ));
        }
    }

    private String generateVerificationToken(String email) {
        // In a real application, use JWT or other secure token method
        // For simplicity, we'll just use a placeholder
        return "verified_" + System.currentTimeMillis() + "_" + email.hashCode();
    }

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        try {
            // Validate username and email
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Username sudah digunakan"));
            }
            
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email sudah terdaftar"));
            }
            
            // Create and save user
            MyAppUser newUser = new MyAppUser();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Hash password
            newUser.setPhoneNumber(registerRequest.getPhoneNumber());
            newUser.setRole(registerRequest.getRole() != null ? (String) registerRequest.getRole() : "USER"); // Default role USER
            
            userRepository.save(newUser);
            
            // Clear OTP after successful registration
            otpService.clearOtp(registerRequest.getEmail());
            
            return ResponseEntity.ok(Map.of(
                "message", "Registrasi berhasil. Silakan login.",
                "username", newUser.getUsername()
            ));
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Registrasi gagal: " + e.getMessage()));
        }
    }
    
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        Optional<MyAppUser> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isEmpty()) {
            logger.warn("Username tidak ditemukan: {}", loginRequest.getUsername());
            return ResponseEntity.badRequest().body("Username tidak ditemukan!");
        }
    
        MyAppUser user = userOptional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("Password salah untuk username: {}", loginRequest.getUsername());
            return ResponseEntity.badRequest().body("Password salah!");
        }
    
        // Generate real JWT token
        String token = jwtService.generateToken(user);
        logger.info("Successfully generated JWT token for user: {}", user.getUsername());
    
        // Tambahkan userId ke dalam response
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("userId", user.getId());  // Pastikan ini ada
        response.put("username", user.getUsername());
        response.put("roomId", user.getRoomId() != null ? user.getRoomId().toString() : "Belum memilih kamar");

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
        user.setRoomId((Long) request.getRoomId()); // Explicitly cast to Long
        userRepository.save(user);
    
        // Kembalikan data user terbaru, termasuk roomId yang baru
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Room ID berhasil diperbarui!");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("roomId", user.getRoomId());
    
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getAllUsers() {
        try {
            List<MyAppUser> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to retrieve users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<?> deleteUser(Long userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(Map.of("message", "User tidak ditemukan"));
            }
            
            userRepository.deleteById(userId);
            return ResponseEntity.ok(Map.of("message", "User berhasil dihapus"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("message", "Gagal menghapus user: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> updateUser(Long userId, RegisterRequest request) {
        try {
            // Check if user exists
            Optional<MyAppUser> userOptional = userRepository.findById(userId);
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body("User not found with ID: " + userId);
            }
            
            MyAppUser user = userOptional.get();
            
            // Update user data
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            // Only update password if provided and not empty
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            
            // Save updated user
            userRepository.save(user);
            
            return ResponseEntity.ok().body(Map.of(
                "message", "User updated successfully",
                "userId", user.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }

    public ResponseEntity<?> resetPassword(Long userId, String newPassword) {
        try {
            // Check if user exists
            Optional<MyAppUser> userOptional = userRepository.findById(userId);
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body("User not found with ID: " + userId);
            }
            
            // Validate the new password
            if (newPassword == null || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body("New password cannot be empty");
            }
            
            MyAppUser user = userOptional.get();
            
            // Update password with encoded version
            user.setPassword(passwordEncoder.encode(newPassword));
            
            // Save updated user
            userRepository.save(user);
            
            return ResponseEntity.ok().body(Map.of(
                "message", "Password reset successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error resetting password: " + e.getMessage());
        }
    }
    
    public ResponseEntity<?> requestRoom(Long userId, AssignRoomRequest request) {
        if (request.getRoomId() == null) {
            return ResponseEntity.badRequest().body("Room ID tidak boleh kosong!");
        }

        Optional<MyAppUser> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Pengguna tidak ditemukan!");
        }
    
        MyAppUser user = userOptional.get();
        user.setRoomId((Long) request.getRoomId());
        user.setRoomRequestStatus("PENDING"); // Set status permintaan kamar ke PENDING
        userRepository.save(user);
    
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Permintaan kamar berhasil dikirim!");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("roomId", user.getRoomId());
        response.put("status", user.getRoomRequestStatus());
    
        return ResponseEntity.ok(response);
    }
    
// Removed duplicate method definition to resolve the compile error

public ResponseEntity<?> approveRoomRequest(Long userId) {
    try {
        Optional<MyAppUser> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Pengguna tidak ditemukan!");
        }
        
        MyAppUser user = userOptional.get();
        if (user.getRoomId() == null) {
            return ResponseEntity.badRequest().body("Pengguna belum memilih kamar!");
        }
        
        user.setRoomRequestStatus("APPROVED");
        userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Permintaan kamar disetujui!");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("roomId", user.getRoomId());
        response.put("status", user.getRoomRequestStatus());
        
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: " + e.getMessage());
    }
}

public ResponseEntity<?> rejectRoomRequest(Long userId) {
    try {
        Optional<MyAppUser> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Pengguna tidak ditemukan!");
        }
        
        MyAppUser user = userOptional.get();
        user.setRoomRequestStatus("REJECTED");
        // Opsional: Hapus roomId jika permintaan ditolak
        // user.setRoomId(null);
        userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Permintaan kamar ditolak!");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("status", user.getRoomRequestStatus());
        
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: " + e.getMessage());
    }
}

public ResponseEntity<?> getPendingRoomRequests() {
    try {
        List<MyAppUser> pendingUsers = userRepository.findByRoomRequestStatus("PENDING");
        return ResponseEntity.ok(pendingUsers);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message", "Gagal mendapatkan daftar permintaan: " + e.getMessage()));
    }
}}
