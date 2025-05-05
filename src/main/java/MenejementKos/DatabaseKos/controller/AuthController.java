package MenejementKos.DatabaseKos.controller;

import java.util.Collections;
import java.util.Map;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import MenejementKos.DatabaseKos.DTO.LoginRequest;
import MenejementKos.DatabaseKos.DTO.RegisterRequest;
import MenejementKos.DatabaseKos.DTO.VerifyOtpRequest;
import MenejementKos.DatabaseKos.Service.OtpService;
import MenejementKos.DatabaseKos.Service.UserService;
import MenejementKos.DatabaseKos.model.MyAppUser;
import MenejementKos.DatabaseKos.model.MyAppUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
    "http://localhost:3000",
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
        authCookie.setSecure("prod".equals(System.getenv("SPRING_PROFILES_ACTIVE")));  // HTTPS only in production
        authCookie.setPath("/");
        authCookie.setMaxAge(0);  // Set cookie expired
        
        response.addCookie(authCookie);
        
        return ResponseEntity.ok(Collections.singletonMap("message", "Logout successful"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        logger.info("OTP verification request received for email: {}", request.getEmail());
        return userService.verifyOtp(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        logger.info("Login request received for username: {}", loginRequest.getUsername());
        ResponseEntity<?> result = userService.login(loginRequest);
        
        // Jika login berhasil, tambahkan cookie
        Map<String, Object> data = (Map<String, Object>) result.getBody();
        if (data != null && data.get("token") != null) {
            String token = data.get("token").toString();
            
            // Buat cookie dengan setting keamanan
            Cookie authCookie = new Cookie("authToken", token);
            authCookie.setHttpOnly(true);  // Mencegah akses dari JavaScript
            authCookie.setSecure("prod".equals(System.getenv("SPRING_PROFILES_ACTIVE")));  // HTTPS only in production
            authCookie.setPath("/");  // Cookie berlaku untuk semua path
            authCookie.setMaxAge(7 * 24 * 60 * 60);  // 7 hari dalam detik
            authCookie.setAttribute("SameSite", "Strict");  // Mencegah CSRF
            
            // Tambahkan cookie ke response
            response.addCookie(authCookie);
        }
        
        return result;
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
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhoneNumber(request.getPhoneNumber());
            user.setEnabled(true); // Langsung aktif tanpa verifikasi
            
            // Set default role USER
            user.setRole("USER");
            
            userRepository.save(user);
            logger.info("User registered successfully: {}", request.getUsername());
            
            return ResponseEntity.ok(Map.of("message", "Registrasi berhasil. Silakan login."));
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("message", "Registrasi gagal: " + e.getMessage()));
        }
    }
}
