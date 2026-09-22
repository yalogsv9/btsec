package vn.edu.ltweb.springws.vidu2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vn.edu.ltweb.springws.vidu2.security.CustomUserDetailsService;

@Configuration
public class Vidu2SecurityConfig {
    @Bean @Order(2)
    SecurityFilterChain vidu2FilterChain(HttpSecurity http, CustomUserDetailsService users, PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(users);
        provider.setPasswordEncoder(passwordEncoder);
        return http.securityMatcher("/vidu2/**")
                .authenticationProvider(provider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/vidu2/login").permitAll()
                        .requestMatchers("/vidu2/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/vidu2/login").loginProcessingUrl("/vidu2/login")
                        .defaultSuccessUrl("/vidu2", true).failureUrl("/vidu2/login?error=true").permitAll())
                .logout(logout -> logout.logoutUrl("/vidu2/logout").logoutSuccessUrl("/vidu2/login?logout")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID"))
                .build();
    }
}
