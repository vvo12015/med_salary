package net.vrakin.medsalary.controller;

import net.vrakin.medsalary.dto.RegistrationForm;
import net.vrakin.medsalary.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контролер для обробки реєстрації нових користувачів.
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final SecurityUserService securityUserService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param securityUserService сервіс для роботи з користувачами.
     * @param passwordEncoder шифрувальник паролів для збереження захищених паролів.
     */
    @Autowired
    public RegistrationController(SecurityUserService securityUserService, PasswordEncoder passwordEncoder) {
        this.securityUserService = securityUserService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Відображає форму реєстрації.
     *
     * @return назва шаблону форми реєстрації ("registration").
     */
    @GetMapping
    public String registerForm() {
        return "registration";
    }

    /**
     * Обробляє реєстрацію нового користувача.
     *
     * Приймає дані з форми, перетворює їх у користувача, зберігає в базі даних і перенаправляє на сторінку входу.
     *
     * @param form форма реєстрації, що містить дані нового користувача.
     * @return перенаправлення на сторінку входу ("/login").
     */
    @PostMapping
    public String processRegistration(RegistrationForm form) {
        // Зберігає нового користувача з зашифрованим паролем
        securityUserService.save(form.toUser(passwordEncoder));

        // Перенаправлення на сторінку входу
        return "redirect:/login";
    }
}
