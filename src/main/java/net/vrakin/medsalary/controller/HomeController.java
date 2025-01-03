package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.SecurityUser;
import net.vrakin.medsalary.service.SecurityUserService;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Контролер для обробки основних запитів додатку, включаючи сторінки входу, головної сторінки та обробку файлів.
 */
@Controller
@RequestMapping
@Slf4j
public class HomeController {

    private final StorageService storageService;
    private final SecurityUserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param storageService сервіс для роботи із завантаженням і видаленням файлів.
     * @param userService сервіс для роботи з користувачами.
     * @param passwordEncoder шифрувальник паролів для перевірки відповідності пароля.
     */
    @Autowired
    public HomeController(StorageService storageService,
                          SecurityUserService userService,
                          PasswordEncoder passwordEncoder) {
        this.storageService = storageService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Відображає сторінку входу.
     *
     * @return назва шаблону сторінки входу ("login").
     */
    @GetMapping("/login")
    public String login() {
        log.info("Accessing login page");
        return "login";
    }

    /**
     * Відображає головну сторінку (індекс).
     *
     * Додає список користувачів до моделі, перевіряючи відповідність паролів певному значенню.
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва шаблону головної сторінки ("index").
     */
    @GetMapping("/")
    public String index(Model model) {
        log.info("Accessing index page");

        // Отримує список користувачів і перевіряє їх паролі
        List<SecurityUser> users = userService.findAll().stream()
                .peek(u -> u.setAddress(Boolean.toString(passwordEncoder.matches("111", u.getPassword()))))
                .toList();

        model.addAttribute("users", users);

        return "index";
    }

    /**
     * Відображає сторінку з помилкою авторизації.
     *
     * @return назва шаблону сторінки помилки ("error_page").
     */
    @GetMapping("/auth-error")
    public String error() {
        log.info("Accessing error page");
        return "error_page";
    }

    /**
     * Видаляє файл із сервера за його ім'ям.
     *
     * @param fileName ім'я файлу, який потрібно видалити.
     * @return HTTP-відповідь із повідомленням про успіх або помилку видалення.
     */
    @DeleteMapping("/delete/files")
    public ResponseEntity<String> deleteFile(@RequestParam("file") String fileName) {
        try {
            storageService.delete(fileName);
            return ResponseEntity.ok("Файл успішно видалено");
        } catch (Exception e) {
            log.error("Error while deleting file: {}", fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Помилка при видаленні файлу");
        }
    }

    @GetMapping("/home")
    public String home(Model model){
        log.info("Accessing home page");

        return "home";
    }

}
