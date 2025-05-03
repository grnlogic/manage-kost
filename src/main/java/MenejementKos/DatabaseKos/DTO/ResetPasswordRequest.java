package MenejementKos.DatabaseKos.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }
}
