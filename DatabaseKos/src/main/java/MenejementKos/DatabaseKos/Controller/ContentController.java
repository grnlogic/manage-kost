package MenejementKos.DatabaseKos.Controller;

import MenejementKos.DatabaseKos.DTO.LoginRequest;
import MenejementKos.DatabaseKos.DTO.RegisterRequest;
import MenejementKos.DatabaseKos.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Ijinkan permintaan dari frontend
public class ContentController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    System.out.println("LoginRequest diterima: " + loginRequest.getUsername() + ", " + loginRequest.getPassword());
    return userService.login(loginRequest);
}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }
}
