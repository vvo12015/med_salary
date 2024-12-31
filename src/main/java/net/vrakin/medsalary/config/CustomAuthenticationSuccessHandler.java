package net.vrakin.medsalary.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * Кастомний обробник успішної аутентифікації.
 *
 * В залежності від ролі користувача, перенаправляє його на відповідну сторінку після успішного входу.
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Метод викликається після успішної аутентифікації.
     *
     * Перенаправляє користувача на сторінку, що відповідає його ролі:
     * <ul>
     *     <li>Якщо користувач має роль "ROLE_ADMIN", його перенаправляють на `/security-user`.</li>
     *     <li>В інших випадках користувача перенаправляють на головну сторінку `/`.</li>
     * </ul>
     *
     * @param request HTTP-запит.
     * @param response HTTP-відповідь.
     * @param authentication об'єкт аутентифікації, що містить інформацію про аутентифікованого користувача.
     * @throws IOException якщо сталася помилка вводу/виводу.
     * @throws ServletException якщо сталася помилка в обробці сервлета.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Отримуємо список ролей аутентифікованого користувача
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // Перевіряємо, чи має користувач роль "ROLE_ADMIN"
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/security-user");
        } else {
            response.sendRedirect("/");
        }
    }
}
