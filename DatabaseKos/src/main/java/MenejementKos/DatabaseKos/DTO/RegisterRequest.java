package MenejementKos.DatabaseKos.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
}
