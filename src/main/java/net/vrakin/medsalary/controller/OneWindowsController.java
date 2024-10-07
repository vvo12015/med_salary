package net.vrakin.medsalary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/one-window")
public class OneWindowsController {

    @GetMapping
    public String oneWindowPage(){
        return "oneWindow";
    }
}
