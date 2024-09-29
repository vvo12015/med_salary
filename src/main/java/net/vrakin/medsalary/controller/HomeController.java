package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.service.SecurityUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@Slf4j
public class HomeController {

    private SecurityUserService userService;

    /*private StorageService storageService;

    @Autowired
    public HomeController(SecurityUserService userService, StorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }*/

   /* @GetMapping("/admin")
    public String admin(Model model){
        log.info("Accessing admin page");
        model.addAttribute("users", userService.findAll());
        return "admin";
    }

    @GetMapping("/nadmin")
    public ModelAndView newAdminPanel(){
        log.info("Accessing nadmin page");
        ModelAndView mav = new ModelAndView("nadmin");

        mav.addObject("users", userService.findAll());
        mav.addObject("pageTitle", "User Manager");
        return mav;
    }
*/
    @GetMapping("/login")
    public String login(Model model) {
        log.info("Accessing login page");
        model.addAttribute("users", userService.findAll());
        return "login";
    }

    @GetMapping("/")
    public String index(Model model){
        log.info("Accessing index page");
        return "index";
    }

   /* @DeleteMapping("/delete/files")
    public ResponseEntity<String> deleteFile(@RequestParam("file") String fileName) {
        try {
            storageService.delete(fileName);
            return ResponseEntity.ok("Файл успішно видалено");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Помилка при видаленні файлу");
        }
    }*/
}
