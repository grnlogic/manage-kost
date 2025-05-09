package MenejementKos.DatabaseKos.controller;

import MenejementKos.DatabaseKos.DTO.AssignRoomRequest;
import MenejementKos.DatabaseKos.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room-requests")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://kos-app-frontend-rzng-beta.vercel.app",
    "https://vercel.com/geran357s-projects/kos-app-frontend-rzng/HLVtQC4FU14UkwetGQG8xkToB97P",
    "https://backend-kos-app.up.railway.app",
    "manage-kost-production.up.railway.app"
})
public class RoomRequestController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/request")
    public ResponseEntity<?> requestRoom(@RequestParam Long userId, @RequestBody AssignRoomRequest request) {
        return userService.requestRoom(userId, request);
    }
    
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