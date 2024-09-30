package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
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
import net.vrakin.medsalary.domain.SecurityUser;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@Slf4j
public class HomeController {

    private StorageService storageService;
    private SecurityUserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public HomeController(SecurityUserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(Model model) {
        log.info("Accessing login page");

        return "login";
    }

    @GetMapping("/")
    public String index(Model model){
        log.info("Accessing index page");
        List<SecurityUser> users = userService.findAll().stream().map(u->{
            u.setAddress(Boolean.toString(passwordEncoder.matches("111", u.getPassword())));
            return u;
        }).collect(Collectors.toList());

        model.addAttribute("users", userService.findAll());

        return "index";
    }

    @GetMapping("/auth-error")
    public String error(Model model){
        log.info("Accessing error page");
        return "error_page";
    }

    @DeleteMapping("/delete/files")
    public ResponseEntity<String> deleteFile(@RequestParam("file") String fileName) {
        try {
            storageService.delete(fileName);
            return ResponseEntity.ok("Файл успішно видалено");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Помилка при видаленні файлу");
        }
    }
}
