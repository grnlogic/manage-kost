package MenejementKos.DatabaseKos.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignRoomRequest {
    private Long roomId;

    public Long getRoomId() {
        return roomId;
    }
}
