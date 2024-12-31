package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.dto.UserDTO;
import net.vrakin.medsalary.excel.entity.reader.StaffListRecordExcelReader;
import net.vrakin.medsalary.mapper.*;
import net.vrakin.medsalary.service.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контролер для управління сторінкою "Список персоналу".
 *
 * Забезпечує функціонал для завантаження файлів Excel, відображення списку персоналу,
 * позицій користувачів, відділів і премій, а також управління відповідними даними.
 */
@Controller
@RequestMapping("/stafflist")
@Slf4j
public class StaffListController extends AbstractController<StaffListRecord, StaffListRecordDTO> {

    public static final int ELEMENT_MAX_COUNT = 100;

    private final UserMapper userMapper;
    private final PremiumCategoryMapper premiumCategoryMapper;
    private final UserService userService;
    private final UserPositionService userPositionService;
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    private final UserPositionMapper userPositionMapper;
    private final PremiumCategoryService premiumCategoryService;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param userService сервіс для роботи з користувачами.
     * @param userPositionService сервіс для роботи з посадами користувачів.
     * @param staffListRecordService сервіс для роботи із записами списку персоналу.
     * @param departmentService сервіс для роботи з відділами.
     * @param storageService сервіс для роботи із завантаженням файлів.
     * @param staffListRecordExcelReader зчитувач Excel-файлів для списку персоналу.
     * @param staffListRecordMapper маппер для перетворення між сутностями StaffListRecord та DTO.
     * @param departmentMapper маппер для перетворення між сутностями Department та DTO.
     * @param userPositionMapper маппер для перетворення між сутностями UserPosition та DTO.
     * @param premiumCategoryService сервіс для роботи з категоріями премій.
     * @param userMapper маппер для перетворення між сутностями User та DTO.
     * @param premiumCategoryMapper маппер для перетворення між сутностями PremiumCategory та DTO.
     */
    public StaffListController(UserService userService, UserPositionService userPositionService,
                               StaffListRecordService staffListRecordService, DepartmentService departmentService,
                               StorageService storageService, StaffListRecordExcelReader staffListRecordExcelReader,
                               StaffListRecordMapper staffListRecordMapper, DepartmentMapper departmentMapper,
                               UserPositionMapper userPositionMapper, PremiumCategoryService premiumCategoryService,
                               UserMapper userMapper, PremiumCategoryMapper premiumCategoryMapper) {
        super("stafflist", storageService, staffListRecordExcelReader, staffListRecordService, staffListRecordMapper);
        this.userService = userService;
        this.userPositionService = userPositionService;
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
        this.userPositionMapper = userPositionMapper;
        this.premiumCategoryService = premiumCategoryService;
        this.userMapper = userMapper;
        this.premiumCategoryMapper = premiumCategoryMapper;
    }

    /**
     * Відображає сторінку "Список персоналу".
     *
     * Передає дані про список персоналу, користувачів, позиції, відділи та категорії премій у модель.
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва шаблону сторінки ("stafflist").
     */
    @GetMapping
    public String stafflist(Model model) {
        log.info("Accessing new stafflist page");

        // Додає список файлів у модель
        model.addAttribute("files", storageService.loadAll().map(
                        path -> path.getFileName().toString())
                .filter(f -> f.startsWith("stafflist"))
                .collect(Collectors.toList()));

        // Отримує список записів персоналу та додає їх у модель
        List<StaffListRecordDTO> stafflist = mapper.toDtoList(service.findAll());
        model.addAttribute("stafflist", stafflist);

        // Логує інформацію про записи
        stafflist.forEach(st -> log.info("staffRecordDTOId: {}, name: {}", st.getId(), st.getUser().getName()));

        // Додає позиції користувачів, відділи та категорії премій у модель
        model.addAttribute("userPositions", userPositionMapper.toDtoList(userPositionService.findAll()));
        model.addAttribute("departments", departmentMapper.toDtoList(departmentService.findAll()));
        model.addAttribute("premiumCategories", premiumCategoryMapper.toDtoList(premiumCategoryService.findAll()));

        // Додає користувачів у модель (з обмеженням до 100 елементів)
        List<UserDTO> userDTOList = userMapper.toDtoList(userService.findAll());
        int elementCount = userDTOList.size();
        model.addAttribute("users", userDTOList.subList(0, Math.min(elementCount, ELEMENT_MAX_COUNT)));

        return "stafflist";
    }

    /**
     * Обробляє запит на завантаження файлу із сервера.
     *
     * @param filename ім'я файлу для завантаження.
     * @return HTTP-відповідь із файлом або статусом 404, якщо файл не знайдено.
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        return getFile(filename);
    }

    /**
     * Обробляє завантаження файлу зі списком персоналу.
     *
     * Перевіряє файл на відповідність формату, зберігає дані у базу
     * і додає повідомлення про результат у атрибути редиректу.
     *
     * @param file завантажений файл.
     * @param monthYear період у форматі `yyyy-MM`, який використовується для іменування файлу.
     * @param redirectAttributes атрибути редиректу для передачі повідомлень.
     * @return перенаправлення на сторінку "stafflist".
     */
    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam String monthYear,
                                   RedirectAttributes redirectAttributes) {

        // Зберігає дані із файлу у базу та додає результат у redirectAttributes
        saveEntitiesAndSendDtoAndErrors(file, monthYear, redirectAttributes);

        // Додає посади користувачів і відділи у redirectAttributes
        List<UserPosition> userPositions = userPositionService.findAll();
        List<Department> departments = departmentService.findAll();

        redirectAttributes.addFlashAttribute("userPositions", userPositions);
        redirectAttributes.addFlashAttribute("departments", departments);

        return "redirect:/" + entityName;
    }
}
