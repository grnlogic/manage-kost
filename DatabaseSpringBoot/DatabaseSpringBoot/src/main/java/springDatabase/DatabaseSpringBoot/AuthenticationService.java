package springDatabase.DatabaseSpringBoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import springDatabase.DatabaseSpringBoot.model.User;
import springDatabase.DatabaseSpringBoot.repository.UserRepository;
import springDatabase.DatabaseSpringBoot.util.JwtUtil;

@Service
public class AuthenticationService { 

    @Autowired
    private UserRepository userRepository; 
    
    @Autowired
    private PasswordEncoder passwordEncoder; 
    
    @Autowired
    private JwtUtil jwtUtil; 

    public String login(String username, String password) { 
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) { 
            throw new BadCredentialsException("Password salah");
        }

        return jwtUtil.generateToken(user); 
    }
}
