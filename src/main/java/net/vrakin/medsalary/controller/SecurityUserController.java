package net.vrakin.medsalary.controller;

import net.vrakin.medsalary.domain.SecurityUser;
import net.vrakin.medsalary.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Контролер для управління безпековими користувачами.
 *
 * Відповідає за отримання списку користувачів і передачу цих даних на фронтенд.
 */
@Controller
@RequestMapping("/security-user")
public class SecurityUserController {

    private final SecurityUserService securityUserService;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param securityUserService сервіс для роботи з користувачами безпеки.
     */
    @Autowired
    public SecurityUserController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    /**
     * Обробляє запит на отримання всіх користувачів.
     *
     * Передає список користувачів у модель для відображення на сторінці.
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва шаблону сторінки ("s_users").
     */
    @GetMapping
    public String getAllUsers(Model model) {
        // Отримує список усіх користувачів
        List<SecurityUser> users = securityUserService.findAll();

        // Додає список користувачів у модель
        model.addAttribute("users", users);

        // Повертає назву шаблону сторінки
        return "s_users";
    }
}
