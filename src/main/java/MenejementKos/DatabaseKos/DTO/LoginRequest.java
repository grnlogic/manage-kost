package MenejementKos.DatabaseKos.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;

    // Explicit getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
