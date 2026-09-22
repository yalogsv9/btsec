package vn.edu.ltweb.springws.vidu2.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.ltweb.springws.vidu2.model.AppUser;
public interface AppUserRepository extends JpaRepository<AppUser, Long> { Optional<AppUser> findByUsername(String username); Optional<AppUser> findByUsernameOrEmail(String username, String email); }
