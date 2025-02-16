package springDatabase.DatabaseSpringBoot; // ✅ Tambahkan ini


import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Setter
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
}
