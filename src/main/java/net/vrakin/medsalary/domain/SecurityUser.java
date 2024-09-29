package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "security_user")
public class SecurityUser implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    private String password;

    @Enumerated(EnumType.STRING)
    private SecurityRole securityRole;

    private String email;
    private String phone;
    private String address;
    private Boolean isEnabled;

    public SecurityUser(String login, String password,
                        SecurityRole securityRole, String email,
                        String phone, String address, Boolean isEnabled) {
        this.login = login;
        this.password = password;
        this.securityRole = securityRole;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.isEnabled = isEnabled;
    }

    public SecurityUser(String login, String encode, String email, String phone, String address) {
        this.login = login;
        this.password = encode;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(securityRole.name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
