package MenejementKos.DatabaseKos.Controller;

import MenejementKos.DatabaseKos.DTO.LoginRequest;
import MenejementKos.DatabaseKos.DTO.RegisterRequest;
import MenejementKos.DatabaseKos.Service.UserService;
import MenejementKos.DatabaseKos.DTO.AssignRoomRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://kos-app-frontend.vercel.app"
}) // Ijinkan permintaan dari frontend
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

    @PutMapping("/users/{userId}/assign-room")
    public ResponseEntity<?> assignRoom(
        @PathVariable Long userId,
        @RequestBody AssignRoomRequest request
    ) {
        return userService.assignRoom(userId, request);
    }
    
}
