package vn.edu.ltweb.springws.vidu1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            @Qualifier("userDetailsService") UserDetailsService users,
                                            PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(users);
        provider.setPasswordEncoder(passwordEncoder);
        return http
                .securityMatcher("/vidu1/**", "/css/**")
                .authenticationProvider(provider)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/vidu1", "/vidu1/login").permitAll()
                        .requestMatchers("/vidu1/private").authenticated()
                        .anyRequest().denyAll())
                .formLogin(form -> form
                        .loginPage("/vidu1/login")
                        .loginProcessingUrl("/vidu1/login")
                        .defaultSuccessUrl("/vidu1/private", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/vidu1/logout")
                        .logoutSuccessUrl("/vidu1"))
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("student")
                        .password(passwordEncoder.encode("123456"))
                        .roles("STUDENT")
                        .build());
    }
}
