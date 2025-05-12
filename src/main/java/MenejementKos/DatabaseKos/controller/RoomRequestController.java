package MenejementKos.DatabaseKos.controller;

import MenejementKos.DatabaseKos.DTO.AssignRoomRequest;
import MenejementKos.DatabaseKos.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/room-requests")
@CrossOrigin(origins = "*")
public class RoomRequestController {

    @Autowired
    private UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(RoomRequestController.class);
    
    // Endpoint untuk mengecek apakah controller berfungsi
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok("RoomRequestController berfungsi dengan baik!");
    }
    
    @PostMapping("/request")
    public ResponseEntity<?> requestRoom(@RequestParam Long userId, @RequestBody AssignRoomRequest request) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }
        
        logger.info("Room request received for user: {}, room: {}", userId, request.getRoomId());
        return userService.requestRoom(userId, request);
    }
    // Endpoint untuk mendapatkan semua permintaan kamar
    @PostMapping("/approve/{userId}")
    public ResponseEntity<?> approveRequest(@PathVariable Long userId) {
        return userService.approveRoomRequest(userId);
    }
    
    @PostMapping("/reject/{userId}")
    public ResponseEntity<?> rejectRequest(@PathVariable Long userId) {
        return userService.rejectRoomRequest(userId);
    }
    
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingRequests() {
        return userService.getPendingRoomRequests();
    }
}