package vn.edu.ltweb.springws.vidu2.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.ltweb.springws.vidu2.model.AppUser;

public class CustomUserDetails implements UserDetails {
    private final AppUser user;
    public CustomUserDetails(AppUser user) { this.user = user; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority(user.getRole().getName())); }
    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isEnabled() { return user.isEnabled(); }
    public String getEmail() { return user.getEmail(); }
    public String getFullName() { return user.getFullName(); }
    public String getImages() { return user.getImages(); }
    public String getRole() { return user.getRole().getName(); }
}
