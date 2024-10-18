package net.vrakin.medsalary.controller;

import net.vrakin.medsalary.mapper.DepartmentMapper;
import net.vrakin.medsalary.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/one-window")
public class OneWindowsController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @Autowired
    public OneWindowsController(DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping
    public String oneWindowPage(Model model){
        model.addAttribute("departments", departmentMapper.toDtoList(departmentService.findAll()));
        return "oneWindow";
    }
}
