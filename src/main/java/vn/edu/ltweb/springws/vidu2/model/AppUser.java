package vn.edu.ltweb.springws.vidu2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 50) private String username;
    @Column(nullable = false, length = 150) private String email;
    @Column(nullable = false) private String password;
    @Column(name = "full_name", nullable = false, length = 150) private String fullName;
    @Column(length = 500) private String images;
    @Column(nullable = false) private boolean enabled = true;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "role_id", nullable = false) private Role role;
    protected AppUser() { }
    public AppUser(String username, String email, String password, String fullName, String images, Role role) { this.username=username; this.email=email; this.password=password; this.fullName=fullName; this.images=images; this.role=role; }
    public Long getId(){return id;} public String getUsername(){return username;} public String getEmail(){return email;} public String getPassword(){return password;} public String getFullName(){return fullName;} public String getImages(){return images;} public boolean isEnabled(){return enabled;} public Role getRole(){return role;}
}
