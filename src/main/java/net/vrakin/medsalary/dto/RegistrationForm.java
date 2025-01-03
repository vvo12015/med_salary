package net.vrakin.medsalary.dto;

import lombok.Data;
import net.vrakin.medsalary.domain.SecurityRole;
import net.vrakin.medsalary.domain.SecurityUser;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * DTO (Data Transfer Object) для форми реєстрації користувача.
 *
 * Використовується для збору даних про нового користувача під час реєстрації.
 */
@Data
public class RegistrationForm {

    /**
     * Логін нового користувача.
     */
    private String login;

    /**
     * Пароль нового користувача.
     */
    private String password;

    /**
     * Електронна пошта нового користувача.
     */
    private String email;

    /**
     * Номер телефону нового користувача.
     */
    private String phone;

    /**
     * Адреса нового користувача.
     */
    private String address;

    /**
     * Перетворює форму реєстрації у об'єкт `SecurityUser`.
     *
     * @param passwordEncoder об'єкт для хешування пароля.
     * @return створений об'єкт `SecurityUser` зі значеннями з форми.
     */
    public SecurityUser toUser(PasswordEncoder passwordEncoder) {
        return new SecurityUser(
                null,
                login,
                passwordEncoder.encode(password), // Хешування пароля
                SecurityRole.USER, // Призначення ролі "USER" за замовчуванням
                email,
                phone,
                address,
                true // Активний стан користувача
        );
    }
}
