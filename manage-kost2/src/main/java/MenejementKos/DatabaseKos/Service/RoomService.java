package MenejementKos.DatabaseKos.Service;

import MenejementKos.DatabaseKos.model.kamar;
import MenejementKos.DatabaseKos.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<kamar> getAllRooms() {
        return roomRepository.findAll();
    }

    public kamar getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public kamar saveRoom(kamar room) {
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}
