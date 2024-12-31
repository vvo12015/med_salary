package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.dto.ServicePackageDTO;
import net.vrakin.medsalary.excel.entity.reader.ServicePackageExcelReader;
import net.vrakin.medsalary.mapper.ServicePackageMapper;
import net.vrakin.medsalary.service.ServicePackageService;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

/**
 * Контролер для управління сторінкою "Пакети послуг".
 *
 * Забезпечує функціонал для відображення сторінки, роботи з файлами Excel,
 * завантаження даних у базу даних і завантаження файлів із сервера.
 */
@Controller
@RequestMapping("/service-package")
@Slf4j
public class ServicePackageController extends AbstractController<ServicePackage, ServicePackageDTO> {

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param storageService сервіс для роботи із завантаженням файлів.
     * @param servicePackageExcelReader зчитувач Excel-файлів для пакетів послуг.
     * @param servicePackageService сервіс для роботи з пакетами послуг.
     * @param servicePackageMapper маппер для перетворення між сутністю та DTO.
     */
    @Autowired
    public ServicePackageController(StorageService storageService,
                                    ServicePackageExcelReader servicePackageExcelReader,
                                    ServicePackageService servicePackageService,
                                    ServicePackageMapper servicePackageMapper) {
        super("service-package", storageService, servicePackageExcelReader, servicePackageService, servicePackageMapper);
    }

    /**
     * Відображає сторінку для управління пакетами послуг.
     *
     * Передає список доступних файлів і повідомлення у модель.
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва шаблону сторінки ("service-package").
     */
    @GetMapping
    public String servicePackage(Model model) {
        log.info("Accessing service package page");

        // Передає список файлів у модель
        model.addAttribute("files", storageService
                .loadAll()
                .filter(p -> p.getFileName().toString().startsWith(entityName))
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList()));

        // Додає повідомлення про успішне завантаження файлу
        model.addAttribute("msg_file", "file upload successfully");
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
     * Обробляє завантаження файлу з пакетами послуг.
     *
     * Перевіряє файл на відповідність формату, зберігає дані в базу
     * і додає повідомлення про результат у атрибути редиректу.
     *
     * @param file завантажений файл.
     * @param monthYear період у форматі `yyyy-MM`, який використовується для іменування файлу.
     * @param redirectAttributes атрибути редиректу для передачі повідомлень.
     * @return перенаправлення на сторінку "service-package".
     */
    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam String monthYear,
                                   RedirectAttributes redirectAttributes) {

        log.info("Accessing post files {} page", entityName);

        // Збереження даних із файлу у базу та передача результату
        saveEntitiesAndSendDtoAndErrors(file, monthYear, redirectAttributes);

        return "redirect:/" + entityName;
    }
}
