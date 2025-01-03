package net.vrakin.medsalary.dto;

import lombok.*;

/**
 * DTO (Data Transfer Object) для сутності SecurityUser.
 *
 * Використовується для передачі даних про користувачів безпеки між рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SecurityUserDTO {

    /**
     * Унікальний ідентифікатор користувача.
     */
    private Long id;

    /**
     * Логін користувача.
     */
    private String login;

    /**
     * Пароль користувача (може бути в хешованому вигляді).
     */
    private String password;

    /**
     * Роль користувача (наприклад, "ADMIN", "USER").
     */
    private String securityRole;

    /**
     * Електронна пошта користувача.
     */
    private String email;

    /**
     * Номер телефону користувача.
     */
    private String phone;

    /**
     * Адреса користувача.
     */
    private String address;
}
