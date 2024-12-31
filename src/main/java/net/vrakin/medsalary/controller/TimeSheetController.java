package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.TimeSheet;
import net.vrakin.medsalary.dto.TimeSheetDTO;
import net.vrakin.medsalary.excel.entity.reader.TimeSheetExcelReader;
import net.vrakin.medsalary.mapper.TimeSheetMapper;
import net.vrakin.medsalary.service.StorageService;
import net.vrakin.medsalary.service.TimeSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

/**
 * Контролер для управління табелями обліку робочого часу (TimeSheets).
 *
 * Забезпечує функціонал для завантаження даних із файлів,
 * відображення табелів та управління ними через сторінку.
 */
@Controller
@RequestMapping("/timesheet")
@Slf4j
public class TimeSheetController extends AbstractController<TimeSheet, TimeSheetDTO> {

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param timeSheetService сервіс для роботи з табелями обліку робочого часу.
     * @param storageService сервіс для роботи із завантаженням файлів.
     * @param timeSheetMapper маппер для перетворення між сутністю TimeSheet та DTO.
     * @param timeSheetExcelReader зчитувач Excel-файлів для табелів обліку.
     */
    @Autowired
    public TimeSheetController(TimeSheetService timeSheetService, StorageService storageService,
                               TimeSheetMapper timeSheetMapper, TimeSheetExcelReader timeSheetExcelReader) {
        super("timesheet", storageService, timeSheetExcelReader, timeSheetService, timeSheetMapper);
    }

    /**
     * Відображає сторінку для управління табелями обліку робочого часу.
     *
     * Передає у модель дані про табелі, отримані з бази даних.
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва шаблону сторінки ("timesheet").
     */
    @GetMapping
    public String userPosition(Model model) {
        log.info("Accessing {} page", entityName);

        // Передає дані у модель
        sendDto(model);

        // Логує, чи є дані в моделі
        if (Objects.nonNull(model.getAttribute(entityName))) {
            log.info("entities exist");
        } else {
            log.info("entities {} not exist", entityName);
        }

        return entityName;
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
     * Обробляє завантаження файлу з табелями обліку робочого часу.
     *
     * Перевіряє файл на відповідність формату, зберігає дані у базу
     * і додає повідомлення про результат у атрибути редиректу.
     *
     * @param file завантажений файл.
     * @param monthYear період у форматі `yyyy-MM`, який використовується для іменування файлу.
     * @param redirectAttributes атрибути редиректу для передачі повідомлень.
     * @return перенаправлення на сторінку "timesheet".
     */
    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam String monthYear,
                                   RedirectAttributes redirectAttributes) {

        log.info("Accessing post files {} page", entityName);

        // Зберігає дані із файлу у базу та додає результат у redirectAttributes
        saveEntitiesAndSendDtoAndErrors(file, monthYear, redirectAttributes);

        return "redirect:/" + entityName;
    }
}
