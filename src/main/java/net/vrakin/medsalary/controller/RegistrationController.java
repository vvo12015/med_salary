package net.vrakin.medsalary.controller;

import net.vrakin.medsalary.dto.RegistrationForm;
import net.vrakin.medsalary.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final SecurityUserService securityUserService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(SecurityUserService securityUserService, PasswordEncoder passwordEncoder) {
        this.securityUserService = securityUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form){
        securityUserService.save(form.toUser(passwordEncoder));

        return "redirect:/login";
    }
}
