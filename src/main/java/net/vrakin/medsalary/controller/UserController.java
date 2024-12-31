package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Контролер для управління сторінкою "Користувачі".
 *
 * Забезпечує функціонал для відображення списку користувачів на сторінці.
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Конструктор для ініціалізації залежності.
     *
     * @param userService сервіс для роботи з користувачами.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Відображає сторінку користувачів.
     *
     * Передає список усіх користувачів у модель для відображення на сторінці.
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва шаблону сторінки ("user").
     */
    @GetMapping
    public String user(Model model) {
        log.info("Accessing user page");

        // Отримує список користувачів
        List<User> users = userService.findAll();

        // Додає список користувачів у модель
        model.addAttribute("users", users);

        // Логує інформацію про кожного користувача
        users.forEach(u -> log.info(u.toString()));

        // Повертає назву шаблону сторінки
        return "user";
    }
}
