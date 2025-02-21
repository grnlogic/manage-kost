package Manajementkost.dto;

import lombok.Getter;
import lombok.Setter;
import Manajementkost.entity.Role; // Tambahkan impor ini

@Getter
@Setter
public class Registerrequest {
    private String username;
    private String password;
    private String email;
    private String name;
    private Role role;
}
