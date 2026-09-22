package vn.edu.ltweb.springws.vidu2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.ltweb.springws.vidu2.model.AppUser;
import vn.edu.ltweb.springws.vidu2.model.Role;
import vn.edu.ltweb.springws.vidu2.repository.AppUserRepository;
import vn.edu.ltweb.springws.vidu2.repository.RoleRepository;

@Configuration
class Vidu2DataInitializer {
    @Bean CommandLineRunner seedVidu2(RoleRepository roles, AppUserRepository users, PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(new Role("ROLE_USER")));
            if (users.findByUsername("user01").isEmpty()) {
                users.save(new AppUser("user01", "user01@gmail.com", passwordEncoder.encode("123456"), "Nguyễn Hữu Trung", "/images/avatar-default.svg", userRole));
            }
        };
    }
}
