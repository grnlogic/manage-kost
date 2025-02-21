package Manajementkost.controller;

import Manajementkost.dto.AuthRequest;
import Manajementkost.dto.Registerrequest;
import Manajementkost.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody Registerrequest registerRequest) {
        return authService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody AuthRequest authRequest) {
        return authService.loginUser(authRequest);
    }
}
