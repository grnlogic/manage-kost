package MenejementKos.DatabaseKos.Service;

import MenejementKos.DatabaseKos.DTO.RegisterRequest;
import MenejementKos.DatabaseKos.model.MyAppUser;
import MenejementKos.DatabaseKos.model.MyAppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * White-box Testing untuk UserService (Partial)
 * Testing method register dan updateUser dengan berbagai kondisi
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private MyAppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OtpService otpService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    private RegisterRequest validRegisterRequest;
    private MyAppUser existingUser;

    @BeforeEach
    void setUp() {
        validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setUsername("testuser");
        validRegisterRequest.setEmail("test@example.com");
        validRegisterRequest.setPassword("password123");
        validRegisterRequest.setPhoneNumber("081234567890");
        validRegisterRequest.setRole("USER");

        existingUser = new MyAppUser();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("encodedPassword");
        existingUser.setPhoneNumber("081234567890");
        existingUser.setRole("USER");
    }

    /**
     * Test Case 1: Register dengan data valid
     * Path: register -> validasi pass -> encode password -> save -> clear OTP -> success
     */
    @Test
    void testRegister_Success() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(MyAppUser.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(otpService).clearOtp(anyString());

        ResponseEntity<?> response = userService.register(validRegisterRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Registrasi berhasil. Silakan login.", body.get("message"));
        assertEquals("testuser", body.get("username"));

        verify(userRepository, times(1)).save(any(MyAppUser.class));
        verify(otpService, times(1)).clearOtp("test@example.com");
    }

    /**
     * Test Case 2: Register dengan username sudah ada
     * Path: register -> existsByUsername true -> return error
     */
    @Test
    void testRegister_UsernameExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        ResponseEntity<?> response = userService.register(validRegisterRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Username sudah digunakan", body.get("message"));

        verify(userRepository, never()).save(any());
        verify(otpService, never()).clearOtp(anyString());
    }

    /**
     * Test Case 3: Register dengan email sudah ada
     * Path: register -> existsByEmail true -> return error
     */
    @Test
    void testRegister_EmailExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        ResponseEntity<?> response = userService.register(validRegisterRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Email sudah terdaftar", body.get("message"));

        verify(userRepository, never()).save(any());
        verify(otpService, never()).clearOtp(anyString());
    }

    /**
     * Test Case 4: Register dengan role null - set default USER
     * Path: register -> role null -> set default "USER"
     */
    @Test
    void testRegister_NullRole_SetDefault() {
        validRegisterRequest.setRole(null);
        
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(MyAppUser.class))).thenAnswer(invocation -> {
            MyAppUser user = invocation.getArgument(0);
            assertEquals("USER", user.getRole(), "Role harus di-set ke USER jika null");
            return user;
        });
        doNothing().when(otpService).clearOtp(anyString());

        ResponseEntity<?> response = userService.register(validRegisterRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).save(any(MyAppUser.class));
    }

    /**
     * Test Case 5: Register dengan exception saat save
     * Path: register -> save throws exception -> catch -> return error
     */
    @Test
    void testRegister_SaveException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(MyAppUser.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = userService.register(validRegisterRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertTrue(body.get("message").toString().contains("Registrasi gagal"));
        
        verify(otpService, never()).clearOtp(anyString());
    }

    /**
     * Test Case 6: Update user dengan data valid
     * Path: updateUser -> user exists -> update fields -> save
     */
    @Test
    void testUpdateUser_Success() {
        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPhoneNumber("089999999999");
        updateRequest.setPassword("newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(MyAppUser.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<?> response = userService.updateUser(1L, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("User updated successfully", body.get("message"));

        verify(userRepository, times(1)).save(any(MyAppUser.class));
    }

    /**
     * Test Case 7: Update user yang tidak ada
     * Path: updateUser -> user not found -> return error
     */
    @Test
    void testUpdateUser_UserNotFound() {
        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPhoneNumber("089999999999");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.updateUser(999L, updateRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));

        verify(userRepository, never()).save(any());
    }

    /**
     * Test Case 8: Update user tanpa mengubah password (password null)
     * Path: updateUser -> password null -> skip password update -> save
     */
    @Test
    void testUpdateUser_NullPassword_SkipPasswordUpdate() {
        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPhoneNumber("089999999999");
        updateRequest.setPassword(null); // No password update

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(MyAppUser.class))).thenAnswer(invocation -> {
            MyAppUser user = invocation.getArgument(0);
            // Password harus tetap sama (tidak di-update)
            assertEquals("encodedPassword", user.getPassword());
            return user;
        });

        ResponseEntity<?> response = userService.updateUser(1L, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, times(1)).save(any(MyAppUser.class));
    }

    /**
     * Test Case 9: Update user dengan password empty string
     * Path: updateUser -> password empty -> skip password update -> save
     */
    @Test
    void testUpdateUser_EmptyPassword_SkipPasswordUpdate() {
        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPhoneNumber("089999999999");
        updateRequest.setPassword(""); // Empty password

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(MyAppUser.class))).thenAnswer(invocation -> {
            MyAppUser user = invocation.getArgument(0);
            assertEquals("encodedPassword", user.getPassword());
            return user;
        });

        ResponseEntity<?> response = userService.updateUser(1L, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(passwordEncoder, never()).encode(any());
    }

    /**
     * Test Case 10: Reset password dengan user valid
     * Path: resetPassword -> user exists -> encode new password -> save
     */
    @Test
    void testResetPassword_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(MyAppUser.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<?> response = userService.resetPassword(1L, "newPassword123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Password reset successful", body.get("message"));

        verify(userRepository, times(1)).save(any(MyAppUser.class));
        verify(passwordEncoder, times(1)).encode("newPassword123");
    }

    /**
     * Test Case 11: Reset password untuk user tidak ada
     * Path: resetPassword -> user not found -> return error
     */
    @Test
    void testResetPassword_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.resetPassword(999L, "newPassword123");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    /**
     * Test Case 12: Reset password dengan password null
     * Path: resetPassword -> password null -> return error
     */
    @Test
    void testResetPassword_NullPassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        ResponseEntity<?> response = userService.resetPassword(1L, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("cannot be empty"));

        verify(userRepository, never()).save(any());
    }

    /**
     * Test Case 13: Reset password dengan password empty
     * Path: resetPassword -> password empty -> return error
     */
    @Test
    void testResetPassword_EmptyPassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        ResponseEntity<?> response = userService.resetPassword(1L, "");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("cannot be empty"));

        verify(userRepository, never()).save(any());
    }

    /**
     * Test Case 14: Delete user yang ada
     * Path: deleteUser -> user exists -> delete -> success
     */
    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        ResponseEntity<?> response = userService.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("User berhasil dihapus", body.get("message"));

        verify(userRepository, times(1)).deleteById(1L);
    }

    /**
     * Test Case 15: Delete user yang tidak ada
     * Path: deleteUser -> user not exists -> return error
     */
    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        ResponseEntity<?> response = userService.deleteUser(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("User tidak ditemukan", body.get("message"));

        verify(userRepository, never()).deleteById(any());
    }
}
