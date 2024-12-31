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

/**
 * Сутність "Користувач безпеки" (SecurityUser).
 *
 * Представляє користувача у системі безпеки, що відповідає за автентифікацію та авторизацію.
 * Реалізує інтерфейс {@link UserDetails}, який інтегрує цей клас із Spring Security.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "security_user")
public class SecurityUser implements UserDetails {

    /**
     * Унікальний ідентифікатор користувача.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Унікальне ім'я користувача (логін).
     * Не може бути порожнім.
     */
    @Column(unique = true, nullable = false)
    private String login;

    /**
     * Пароль користувача (у зашифрованому вигляді).
     */
    private String password;

    /**
     * Роль користувача, визначена у перерахунку {@link SecurityRole}.
     */
    @Enumerated(EnumType.STRING)
    private SecurityRole securityRole;

    /**
     * Email користувача.
     */
    private String email;

    /**
     * Телефонний номер користувача.
     */
    private String phone;

    /**
     * Адреса користувача.
     */
    private String address;

    /**
     * Вказує, чи увімкнено обліковий запис користувача.
     */
    private Boolean isEnabled;

    /**
     * Конструктор для створення об'єкта користувача з усіма основними параметрами.
     *
     * @param login        Логін користувача.
     * @param password     Пароль користувача (у зашифрованому вигляді).
     * @param securityRole Роль користувача.
     * @param email        Email користувача.
     * @param phone        Телефон користувача.
     * @param address      Адреса користувача.
     * @param isEnabled    Статус облікового запису (увімкнено чи ні).
     */
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

    /**
     * Конструктор для створення об'єкта користувача з мінімальними параметрами.
     *
     * @param login    Логін користувача.
     * @param encode   Пароль користувача (у зашифрованому вигляді).
     * @param email    Email користувача.
     * @param phone    Телефон користувача.
     * @param address  Адреса користувача.
     */
    public SecurityUser(String login, String encode, String email, String phone, String address) {
        this.login = login;
        this.password = encode;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Повертає список повноважень (ролей) користувача.
     *
     * @return Колекція об'єктів {@link GrantedAuthority}, які представляють ролі користувача.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(securityRole.name()));
        return authorities;
    }

    /**
     * Отримує ім'я користувача (логін).
     *
     * @return Логін користувача.
     */
    @Override
    public String getUsername() {
        return this.login;
    }

    /**
     * Вказує, чи увімкнено обліковий запис користувача.
     *
     * @return {@code true}, якщо обліковий запис увімкнено, інакше {@code false}.
     */
    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    // Реалізація інших методів UserDetails (за потреби можна додати):
    /**
     * Вказує, чи не протерміновано дію облікового запису.
     *
     * @return Завжди {@code true}.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Вказує, чи не заблоковано обліковий запис.
     *
     * @return Завжди {@code true}.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Вказує, чи не протерміновано дію пароля.
     *
     * @return Завжди {@code true}.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
