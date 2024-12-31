package net.vrakin.medsalary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфігурація безпеки для додатку.
 *
 * Забезпечує налаштування аутентифікації, авторизації, обробки сесій та початкової ініціалізації даних.
 */
@Configuration
public class SecurityConfig {

    private final InitData initData;
    private final CustomAuthenticationSuccessHandler successHandler;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param initData сервіс для ініціалізації початкових даних.
     */
    @Autowired
    public SecurityConfig(InitData initData) {
        this.initData = initData;
        this.successHandler = new CustomAuthenticationSuccessHandler();
    }

    /**
     * Налаштування фільтра безпеки.
     *
     * <p>Забезпечує:
     * <ul>
     *   <li>Доступ до загальнодоступних сторінок без авторизації.</li>
     *   <li>Обмеження доступу до API та інших ресурсів залежно від ролей користувача.</li>
     *   <li>Налаштування сторінок входу та виходу.</li>
     * </ul>
     *
     * @param http об'єкт для налаштування безпеки.
     * @return конфігурація ланцюга фільтрів безпеки.
     * @throws Exception якщо виникають помилки під час налаштування.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Вимкнення CSRF-захисту
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/login", "/register", "/auth-error").permitAll() // Доступ для всіх
                        .requestMatchers("/webjars/**", "/css/**", "/js/**", "/images/**").permitAll() // Доступ до статичних ресурсів
                        .requestMatchers(HttpMethod.POST, "/security-user").hasRole("ADMIN") // Тільки адміністратор
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER") // Адмін або користувач
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN") // Тільки адміністратор
                        .requestMatchers(HttpMethod.GET, "/index").hasAnyRole("ADMIN", "USER") // Адмін або користувач
                        .anyRequest().authenticated() // Усі інші запити потребують авторизації
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // Сторінка входу
                        .successHandler(successHandler) // Кастомний обробник успішного входу
                        .failureUrl("/auth-error") // Сторінка помилки авторизації
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для виходу
                        .logoutSuccessUrl("/") // Перенаправлення після виходу
                        .invalidateHttpSession(true) // Інвалідація сесії
                        .deleteCookies("JSESSIONID") // Видалення cookie
                        .permitAll()
                )
                .build();
    }

    /**
     * Ініціалізація початкових даних при запуску програми.
     *
     * @return {@link CommandLineRunner}, який викликає метод ініціалізації.
     */
    @Bean
    public CommandLineRunner init() {
        return strings -> initData.init();
    }

    /**
     * Шифрувальник паролів.
     *
     * Використовується для збереження паролів користувачів у зашифрованому вигляді.
     *
     * @return екземпляр {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
