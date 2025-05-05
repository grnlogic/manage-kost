package MenejementKos.DatabaseKos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import MenejementKos.DatabaseKos.Service.UserService;
import MenejementKos.DatabaseKos.DTO.RegisterRequest;
import MenejementKos.DatabaseKos.DTO.AssignRoomRequest;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://kos-app-frontend-rzng-beta.vercel.app",
    "https://kos-app-frontend.vercel.app"
})
public class UserRedirectController {
    
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        System.out.println("GET /user endpoint called (redirected)");
        return userService.getAllUsers();
    }

    @PutMapping("/{userId}/assign-room")
    public ResponseEntity<?> assignRoom(
        @PathVariable Long userId,
        @RequestBody AssignRoomRequest request
    ) {
        return userService.assignRoom(userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
        @PathVariable Long userId,
        @RequestBody RegisterRequest request
    ) {
        return userService.updateUser(userId, request);
    }
}
