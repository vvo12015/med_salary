package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.dto.DepartmentDTO;
import net.vrakin.medsalary.excel.entity.reader.DepartmentExcelReader;
import net.vrakin.medsalary.mapper.DepartmentMapper;
import net.vrakin.medsalary.mapper.StaffListRecordMapper;
import net.vrakin.medsalary.service.DepartmentService;
import net.vrakin.medsalary.service.StaffListRecordService;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Контролер для управління сторінкою "Відділи" та завантаженням файлів, пов'язаних із відділами.
 *
 * Наслідує функціонал від {@link AbstractController}, додаючи специфічну логіку для роботи з відділами.
 */
@Controller
@RequestMapping("/department")
@Slf4j
public class DepartmentController extends AbstractController<Department, DepartmentDTO> {

    private final StaffListRecordMapper staffListRecordMapper;
    private final StaffListRecordService staffListRecordService;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param storageService сервіс для роботи із завантаженням файлів.
     * @param departmentExcelReader зчитувач Excel-файлів для відділів.
     * @param departmentService сервіс для роботи з відділами.
     * @param staffListRecordMapper маппер для записів списку персоналу.
     * @param staffListRecordService сервіс для роботи із записами списку персоналу.
     * @param departmentMapper маппер для відділів.
     */
    public DepartmentController(StorageService storageService, DepartmentExcelReader departmentExcelReader,
                                DepartmentService departmentService,
                                StaffListRecordMapper staffListRecordMapper,
                                StaffListRecordService staffListRecordService,
                                DepartmentMapper departmentMapper) {
        super("department", storageService, departmentExcelReader, departmentService, departmentMapper);
        this.staffListRecordMapper = staffListRecordMapper;
        this.staffListRecordService = staffListRecordService;
    }

    /**
     * Відображає сторінку з відділами.
     *
     * Передає дані відділів та списку персоналу у модель.
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва сторінки для відображення (назва сутності).
     */
    @GetMapping
    public String departments(Model model) {
        log.info("Accessing department page");

        // Передає дані відділів та список файлів у модель
        sendDto(model);

        // Передає дані списку персоналу у модель
        model.addAttribute("stafflist", staffListRecordMapper.toDtoList(staffListRecordService.findAll()));
        return entityName;
    }

    /**
     * Завантажує файл із сервера.
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
     * Обробляє завантаження Excel-файлу з даними відділів.
     *
     * Зберігає дані з файлу у базу, якщо файл валідний, та додає повідомлення про результат завантаження.
     *
     * @param file завантажений файл.
     * @param monthYear період у форматі `yyyy-MM`, який використовується для іменування файлу.
     * @param redirectAttributes атрибути редиректу для передачі повідомлень.
     * @return перенаправлення на сторінку відділів.
     */
    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam String monthYear,
                                   RedirectAttributes redirectAttributes) {

        log.info("Accessing post files department page");

        // Обробка завантаження файлу, збереження даних у базу та передача результату через redirectAttributes
        saveEntitiesAndSendDtoAndErrors(file, monthYear, redirectAttributes);

        return "redirect:/" + entityName;
    }
}
