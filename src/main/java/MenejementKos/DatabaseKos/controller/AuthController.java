package MenejementKos.DatabaseKos.Controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import MenejementKos.DatabaseKos.DTO.LoginRequest;
import MenejementKos.DatabaseKos.DTO.RegisterRequest;
import MenejementKos.DatabaseKos.DTO.VerifyOtpRequest;
import MenejementKos.DatabaseKos.Service.JwtService;
import MenejementKos.DatabaseKos.Service.OtpService;
import MenejementKos.DatabaseKos.Service.UserService;
import MenejementKos.DatabaseKos.model.MyAppUser;
import MenejementKos.DatabaseKos.model.MyAppUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
    "http://141.11.25.167:3000",
    "https://kos-app-frontend-rzng-beta.vercel.app",
    "https://vercel.com/geran357s-projects/kos-app-frontend-rzng/HLVtQC4FU14UkwetGQG8xkToB97P",
    "https://backend-kos-app.up.railway.app",
    "manage-kost-production.up.railway.app"
})
public class AuthController {
    
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MyAppUserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private OtpService otpService;
    
    @Autowired
    private JwtService jwtService;

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam String email) {
        logger.info("OTP request received for email: {}", email);
        return userService.requestRegistrationOtp(email);
    }

    
    @PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletResponse response) {
    logger.info("Logout request received");
    
    // Hapus cookie authToken
    Cookie authCookie = new Cookie("authToken", null);
    authCookie.setHttpOnly(true);
    authCookie.setSecure("prod".equals(System.getenv("SPRING_PROFILES_ACTIVE")));
    authCookie.setPath("/");
    authCookie.setMaxAge(0);  // Set cookie expired
    response.addCookie(authCookie);
    
    // Hapus juga cookie isLoggedIn
    Cookie isLoggedInCookie = new Cookie("isLoggedIn", null);
    isLoggedInCookie.setHttpOnly(false);
    isLoggedInCookie.setSecure("prod".equals(System.getenv("SPRING_PROFILES_ACTIVE")));
    isLoggedInCookie.setPath("/");
    isLoggedInCookie.setMaxAge(0);
    response.addCookie(isLoggedInCookie);
    
    return ResponseEntity.ok(Collections.singletonMap("message", "Logout successful"));
}

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    logger.info("Login request received for username: {}", loginRequest.getUsername());
    ResponseEntity<?> result = userService.login(loginRequest);
    
    // Jika login berhasil, tambahkan cookie
    Map<String, Object> data = (Map<String, Object>) result.getBody();
    if (data != null && data.get("token") != null) {
        String token = data.get("token").toString();
        
        logger.info("Setting authToken cookie for user: {}", loginRequest.getUsername());
        
        // Buat cookie authToken dengan setting yang benar
        Cookie authCookie = new Cookie("authToken", token);
        authCookie.setHttpOnly(true);  // Mencegah akses dari JavaScript (keamanan)
        authCookie.setSecure(false);  // Set false untuk development (localhost HTTP)
        authCookie.setPath("/");  // Cookie berlaku untuk semua path
        authCookie.setMaxAge(7 * 24 * 60 * 60);  // 7 hari dalam detik
        // Hanya set SameSite sekali dengan nilai Lax (lebih kompatibel)
        
        response.addCookie(authCookie);
        logger.info("authToken cookie added successfully");
        
        // Tambahkan cookie isLoggedIn yang bisa diakses JavaScript
        Cookie statusCookie = new Cookie("isLoggedIn", "true");
        statusCookie.setHttpOnly(false);  // Bisa diakses oleh JavaScript
        statusCookie.setSecure(false);
        statusCookie.setPath("/");
        statusCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(statusCookie);
        
        logger.info("Login cookies set successfully for user: {}", loginRequest.getUsername());
    } else {
        logger.warn("Login failed or token not generated for user: {}", loginRequest.getUsername());
    }
    
    return result;
}
@GetMapping("/user-info")
public ResponseEntity<?> getUserInfo() {
    // Ambil informasi user dari context keamanan
    Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getName().equals("anonymousUser")) {
        return ResponseEntity.status(401).body(Map.of("message", "Tidak terautentikasi"));
    }
    
    String username = auth.getName();
    
    // Cari user di database
    MyAppUser user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan"));
    
    // Kembalikan informasi user yang dibutuhkan
    Map<String, Object> response = new HashMap<>();
    response.put("username", user.getUsername());
    response.put("email", user.getEmail());
    response.put("role", user.getRole());
    // Tambahkan informasi lain yang dibutuhkan front-end
    response.put("roomId", user.getRoomId());
    
    return ResponseEntity.ok(response);
}

// Tambahkan ke AuthController.java
@PostMapping("/verify-otp")
public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
    return userService.verifyOtp(request);
}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("Registration request received for username: {}", request.getUsername());
        return userService.register(request);
    }

    /**
     * Endpoint sementara untuk registrasi tanpa OTP
     */
    @PostMapping("/register-direct")
    public ResponseEntity<?> registerUserDirect(@RequestBody RegisterRequest request) {
        try {
            logger.info("Received direct registration request for username: {}", request.getUsername());
            logger.info("Request details - Email: {}, PhoneNumber: {}", request.getEmail(), request.getPhoneNumber());
            
            // Validate required fields
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                logger.warn("Username is empty or null");
                return ResponseEntity.badRequest().body(Map.of("message", "Username tidak boleh kosong"));
            }
            
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                logger.warn("Email is empty or null");
                return ResponseEntity.badRequest().body(Map.of("message", "Email tidak boleh kosong"));
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                logger.warn("Password is empty or null");
                return ResponseEntity.badRequest().body(Map.of("message", "Password tidak boleh kosong"));
            }
            
            // Validate email format
            if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                logger.warn("Invalid email format: {}", request.getEmail());
                return ResponseEntity.badRequest().body(Map.of("message", "Format email tidak valid"));
            }
            
            // Validate input
            if (userRepository.existsByUsername(request.getUsername())) {
                logger.warn("Username already taken: {}", request.getUsername());
                return ResponseEntity.badRequest().body(Map.of("message", "Username sudah digunakan"));
            }
            
            if (userRepository.existsByEmail(request.getEmail())) {
                logger.warn("Email already in use: {}", request.getEmail());
                return ResponseEntity.badRequest().body(Map.of("message", "Email sudah terdaftar"));
            }
            
            // Create user without OTP verification
            MyAppUser user = new MyAppUser();
            user.setUsername(request.getUsername().trim());
            user.setEmail(request.getEmail().trim());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            
            // Handle phone number - set default if null or empty
            String phoneNumber = request.getPhoneNumber();
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                phoneNumber = "0000000000"; // Default phone number
                logger.info("Using default phone number for user: {}", request.getUsername());
            }
            user.setPhoneNumber(phoneNumber.trim());
            
            user.setEnabled(true); // Langsung aktif tanpa verifikasi
            
            // Set default role USER
            user.setRole("USER");
            
            userRepository.save(user);
            logger.info("User registered successfully: {} with email: {}", request.getUsername(), request.getEmail());
            
            return ResponseEntity.ok(Map.of(
                "message", "Registrasi berhasil. Silakan login.",
                "username", user.getUsername(),
                "email", user.getEmail()
            ));
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("message", "Registrasi gagal: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletResponse response) {
        // Ambil informasi user dari token yang ada
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName().equals("anonymousUser")) {
            return ResponseEntity.status(401).body(Map.of("message", "Token tidak valid atau expired"));
        }
        
        String username = auth.getName();
        
        // Cari user di database
        MyAppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan"));
        
        // Generate token baru
        String newToken = jwtService.generateToken(user);
        
        // Set cookie baru
        Cookie authCookie = new Cookie("authToken", newToken);
        authCookie.setHttpOnly(true);
        authCookie.setSecure("prod".equals(System.getenv("SPRING_PROFILES_ACTIVE")));
        authCookie.setPath("/");
        authCookie.setMaxAge(7 * 24 * 60 * 60);
        authCookie.setAttribute("SameSite", "Lax"); // Lebih permisif dari Strict
        response.addCookie(authCookie);
        
        return ResponseEntity.ok(Map.of("message", "Token berhasil diperbarui"));
    }
}
