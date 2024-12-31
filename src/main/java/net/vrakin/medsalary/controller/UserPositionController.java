package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.UserPositionDTO;
import net.vrakin.medsalary.excel.entity.reader.UserPositionExcelReader;
import net.vrakin.medsalary.mapper.UserPositionMapper;
import net.vrakin.medsalary.service.StorageService;
import net.vrakin.medsalary.service.UserPositionService;
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
 * Контролер для управління посадами користувачів (User Positions).
 *
 * Забезпечує функціонал для завантаження даних із файлів,
 * відображення посад та управління ними через сторінку.
 */
@Controller
@RequestMapping("/userposition")
@Slf4j
public class UserPositionController extends AbstractController<UserPosition, UserPositionDTO> {

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param userPositionService сервіс для роботи з посадами користувачів.
     * @param storageService сервіс для роботи із завантаженням файлів.
     * @param userPositionMapper маппер для перетворення між сутністю UserPosition та DTO.
     * @param userPositionExcelReader зчитувач Excel-файлів для посад користувачів.
     */
    @Autowired
    public UserPositionController(UserPositionService userPositionService, StorageService storageService,
                                  UserPositionMapper userPositionMapper, UserPositionExcelReader userPositionExcelReader) {
        super("userposition", storageService, userPositionExcelReader, userPositionService, userPositionMapper);
    }

    /**
     * Відображає сторінку для управління посадами користувачів.
     *
     * Передає у модель дані про посади, отримані з бази даних.
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва шаблону сторінки ("userposition").
     */
    @GetMapping
    public String userPosition(Model model) {
        log.info("Accessing {} page", entityName);

        // Передає дані у модель
        sendDto(model);

        // Логує, чи є дані в моделі
        if (Objects.nonNull(model.getAttribute(entityName))) {
            log.info("entities {} exist", entityName + "s");
        } else {
            log.info("entities {} not exist", entityName + "s");
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
     * Обробляє завантаження файлу з посадами користувачів.
     *
     * Перевіряє файл на відповідність формату, зберігає дані у базу
     * і додає повідомлення про результат у атрибути редиректу.
     *
     * @param file завантажений файл.
     * @param monthYear період у форматі `yyyy-MM`, який використовується для іменування файлу.
     * @param redirectAttributes атрибути редиректу для передачі повідомлень.
     * @return перенаправлення на сторінку "userposition".
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
