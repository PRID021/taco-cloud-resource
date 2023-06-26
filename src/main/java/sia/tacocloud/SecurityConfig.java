package sia.tacocloud;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;
import sia.tacocloud.models.TacoUser;
import sia.tacocloud.repositories.OrderAdminService;
import sia.tacocloud.repositories.OrderRepository;
import sia.tacocloud.repositories.UserRepository;

@Configuration
@Slf4j
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /*
         * csrf().disable() - For an unexpected error (type=Forbidden, status=403) -
         * Forbidden
         * when perform POST request to /design or /orders.
         * By default, Spring Security will protect against CRSF attacks by requiring a
         * CSRF token to be sent with every request that could be processed by a
         * browser. If you’re using Thymeleaf, Spring Security automatically includes a
         * CSRF token in the model for any page that includes a form tag.
         */

        return http
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/api/ingredients").hasRole("SCOPE_writeIngredients")
                .requestMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/ingredients").hasAuthority("SCOPE_writeIngredients")
                .requestMatchers(HttpMethod.DELETE, "/api/ingredients").hasAuthority("SCOPE_deleteIngredients")
                .requestMatchers("/design", "/orders", "/").hasRole("USER")
                .requestMatchers("/", "/**").permitAll()
                .and()
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(java.util.List.of("*"));
                    corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE"));
                    corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                    return corsConfiguration;
                }))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("scrypt@SpringSecurity_v5_8", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
        return passwordEncoder;
    }

    @Bean
    public OrderAdminService orderAdminService(OrderRepository orderRepository) {
        return orderRepository::deleteAll;
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            log.error("Find user: " + username);
            TacoUser user = userRepo.findByUsername(username);
            log.error("Result find user: " + user);
            if (user != null) {
                return user;
            }
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

}
