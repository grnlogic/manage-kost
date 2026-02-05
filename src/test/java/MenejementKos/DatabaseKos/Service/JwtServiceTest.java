package MenejementKos.DatabaseKos.Service;

import MenejementKos.DatabaseKos.model.MyAppUser;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * White-box Testing untuk JwtService
 * Testing berbagai path dan kondisi dalam pembuatan dan validasi JWT token
 */
class JwtServiceTest {

    private JwtService jwtService;
    private MyAppUser testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        
        // Set secret key dan expiration menggunakan reflection
        ReflectionTestUtils.setField(jwtService, "secretKey", "testSecretKeyWhichShouldBeAtLeast32CharactersLongForTesting");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L); // 1 hour
        
        // Setup test user
        testUser = new MyAppUser();
        testUser.setUsername("testuser");
        testUser.setRole("USER");
        testUser.setRoomId(101L);
    }

    /**
     * Test Case 1: Generate token untuk user dengan roomId
     * Path: generateToken -> createToken -> with roomId claim
     */
    @Test
    void testGenerateToken_WithRoomId() {
        String token = jwtService.generateToken(testUser);
        
        assertNotNull(token, "Token tidak boleh null");
        assertTrue(token.length() > 0, "Token harus memiliki konten");
        
        // Verify token contains correct username
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals("testuser", extractedUsername, "Username dalam token harus sesuai");
    }

    /**
     * Test Case 2: Generate token untuk user tanpa roomId
     * Path: generateToken -> createToken -> without roomId claim
     */
    @Test
    void testGenerateToken_WithoutRoomId() {
        testUser.setRoomId(null);
        String token = jwtService.generateToken(testUser);
        
        assertNotNull(token, "Token tidak boleh null meskipun tanpa roomId");
        
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals("testuser", extractedUsername);
    }

    /**
     * Test Case 3: Extract username dari token valid
     * Path: extractUsername -> extractClaim -> extractAllClaims (success)
     */
    @Test
    void testExtractUsername_ValidToken() {
        String token = jwtService.generateToken(testUser);
        String username = jwtService.extractUsername(token);
        
        assertEquals("testuser", username, "Username harus di-extract dengan benar");
    }

    /**
     * Test Case 4: Extract expiration date dari token
     * Path: extractExpiration -> extractClaim -> extractAllClaims
     */
    @Test
    void testExtractExpiration_ValidToken() {
        String token = jwtService.generateToken(testUser);
        Date expiration = jwtService.extractExpiration(token);
        
        assertNotNull(expiration, "Expiration date tidak boleh null");
        assertTrue(expiration.after(new Date()), "Expiration date harus di masa depan");
    }

    /**
     * Test Case 5: Cek token belum expired (valid token)
     * Path: isTokenExpired -> extractExpiration -> false (not expired)
     */
    @Test
    void testIsTokenExpired_NotExpired() {
        String token = jwtService.generateToken(testUser);
        Boolean isExpired = jwtService.isTokenExpired(token);
        
        assertFalse(isExpired, "Token yang baru dibuat seharusnya belum expired");
    }

    /**
     * Test Case 6: Cek token expired (token dengan expiration di masa lalu)
     * Path: isTokenExpired -> extractExpiration -> true (expired)
     */
    @Test
    void testIsTokenExpired_Expired() {
        // Set expiration ke nilai negatif (sudah expired)
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        
        String token = jwtService.generateToken(testUser);
        Boolean isExpired = jwtService.isTokenExpired(token);
        
        assertTrue(isExpired, "Token dengan expiration negatif harus expired");
    }

    /**
     * Test Case 7: Validasi token dengan username yang cocok
     * Path: validateToken -> extractUsername & isTokenExpired -> true (valid)
     */
    @Test
    void testValidateToken_ValidUsernameAndNotExpired() {
        String token = jwtService.generateToken(testUser);
        Boolean isValid = jwtService.validateToken(token, "testuser");
        
        assertTrue(isValid, "Token dengan username cocok dan belum expired harus valid");
    }

    /**
     * Test Case 8: Validasi token dengan username tidak cocok
     * Path: validateToken -> extractUsername (mismatch) -> false (invalid)
     */
    @Test
    void testValidateToken_InvalidUsername() {
        String token = jwtService.generateToken(testUser);
        Boolean isValid = jwtService.validateToken(token, "wronguser");
        
        assertFalse(isValid, "Token dengan username tidak cocok harus invalid");
    }

    /**
     * Test Case 9: Validasi token yang malformed (format salah)
     * Path: validateToken -> exception -> false
     */
    @Test
    void testValidateToken_MalformedToken() {
        String malformedToken = "invalid.token.format";
        Boolean isValid = jwtService.validateToken(malformedToken, "testuser");
        
        assertFalse(isValid, "Token dengan format salah harus invalid");
    }

    /**
     * Test Case 10: Extract claims dari token dengan role dan roomId
     * Path: extractAllClaims dengan custom claims
     */
    @Test
    void testExtractClaim_CustomClaims() {
        String token = jwtService.generateToken(testUser);
        
        // Extract role claim
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        assertEquals("USER", role, "Role claim harus sesuai");
        
        // Extract roomId claim
        Integer roomId = jwtService.extractClaim(token, claims -> claims.get("roomId", Integer.class));
        assertEquals(101, roomId, "RoomId claim harus sesuai");
    }

    /**
     * Test Case 11: Boundary test - token dengan expiration tepat saat ini
     * Path: isTokenExpired -> edge case (expires exactly now)
     */
    @Test
    void testIsTokenExpired_BoundaryCase() {
        // Set expiration sangat pendek (1ms)
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1L);
        
        String token = jwtService.generateToken(testUser);
        
        // Wait untuk token expired
        try {
            Thread.sleep(10); // Sleep 10ms untuk memastikan token expired
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Boolean isExpired = jwtService.isTokenExpired(token);
        assertTrue(isExpired, "Token dengan expiration sangat pendek harus expired");
    }

    /**
     * Test Case 12: Generate multiple tokens untuk user yang sama
     * Path: Testing konsistensi token generation
     */
    @Test
    void testGenerateToken_MultipleTokens() {
        String token1 = jwtService.generateToken(testUser);
        
        // Wait sedikit untuk memastikan timestamp berbeda
        try {
            Thread.sleep(1100); // Increased to 1100ms to ensure different second timestamp
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String token2 = jwtService.generateToken(testUser);
        
        // Note: Tokens might be same if generated within same second
        // What matters is both are valid for the same user
        assertTrue(jwtService.validateToken(token1, "testuser"), "Token 1 harus valid");
        assertTrue(jwtService.validateToken(token2, "testuser"), "Token 2 harus valid");
        
        // Both tokens should have same username
        assertEquals(jwtService.extractUsername(token1), jwtService.extractUsername(token2), 
                    "Kedua token harus memiliki username yang sama");
    }
}
