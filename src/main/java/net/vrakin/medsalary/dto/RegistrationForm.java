package net.vrakin.medsalary.dto;

import lombok.Data;
import net.vrakin.medsalary.domain.SecurityUser;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationForm {
    private String login;
    private String password;
    private String email;
    private String phone;
    private String address;

    public SecurityUser toUser(PasswordEncoder passwordEncoder) {
        return new SecurityUser(login, passwordEncoder.encode(password), email, phone, address);
    }
}
