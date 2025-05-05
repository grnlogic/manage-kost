package MenejementKos.DatabaseKos.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import MenejementKos.DatabaseKos.model.MyAppUserService;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyAppUserService myAppUserService;

    // Konstruktor manual untuk menyuntikkan MyAppUserService
    public SecurityConfig(MyAppUserService myAppUserService) {
        this.myAppUserService = myAppUserService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return myAppUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/**").permitAll()
                .requestMatchers("/api/auth/request-otp", "/api/auth/verify-otp").permitAll() // New OTP endpoints
                .requestMatchers("/api/profile-picture/upload").permitAll()
                .requestMatchers("/api/profile-picture/**").permitAll()
                .requestMatchers("/api/faqs/**").permitAll()
                .requestMatchers("/api/peraturan/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/user/**").permitAll() // Tambahkan endpoint lama /user untuk backward compatibility
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/profiles/**").permitAll()
                .requestMatchers("/api/kamar/**").permitAll()
                .requestMatchers("/api/pengumuman/**").permitAll()
                .requestMatchers("/api/kebersihan/**").permitAll()
                .requestMatchers("/api/rooms/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
            "http://localhost:3000", 
            "https://kos-app-frontend-rzng-beta.vercel.app",
            "https://kos-app-frontend.vercel.app", // Add this
            "https://vercel.com/geran357s-projects/kos-app-frontend-rzng/HLVtQC4FU14UkwetGQG8xkToB97P",
            "https://manage-kost-production.up.railway.app", // Add this with https://
            "https://backend-kos-app.up.railway.app" // Add this if needed
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin")); // Add more headers
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
