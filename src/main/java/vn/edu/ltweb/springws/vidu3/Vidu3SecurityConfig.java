package vn.edu.ltweb.springws.vidu3;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Vidu3SecurityConfig {

    @Bean
    @Order(3)
    SecurityFilterChain vidu3FilterChain(HttpSecurity http,
                                         @Qualifier("vidu3UserDetailsService") UserDetailsService users,
                                         PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(users);
        provider.setPasswordEncoder(passwordEncoder);

        return http
                .securityMatcher("/vidu3/**")
                .authenticationProvider(provider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/vidu3/login").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/vidu3/login")
                        .loginProcessingUrl("/vidu3/login")
                        .defaultSuccessUrl("/vidu3", true)
                        .failureUrl("/vidu3/login?error=true")
                        .permitAll())
                .rememberMe(remember -> remember
                        .key("vidu3-remember-me-key")
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(604800)
                        .userDetailsService(users))
                .logout(logout -> logout
                        .logoutUrl("/vidu3/logout")
                        .logoutSuccessUrl("/vidu3/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me"))
                .build();
    }

    @Bean
    UserDetailsService vidu3UserDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("rememberme")
                        .password(passwordEncoder.encode("123456"))
                        .roles("USER")
                        .build());
    }
}
