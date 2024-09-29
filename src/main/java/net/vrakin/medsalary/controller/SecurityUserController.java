package net.vrakin.medsalary.controller;

import net.vrakin.medsalary.domain.SecurityUser;
import net.vrakin.medsalary.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("security-user")
public class SecurityUserController {

    private SecurityUserService securityUserService;


    @Autowired
    public SecurityUserController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<SecurityUser> users = securityUserService.findAll();
        model.addAttribute("users", users);
        return "s_users";
    }
}
