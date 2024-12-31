package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.dto.NszuDecryptionDTO;
import net.vrakin.medsalary.excel.entity.reader.NszuDecryptionExcelReader;
import net.vrakin.medsalary.mapper.NSZU_DecryptionMapper;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Контролер для роботи зі сторінкою "Розшифровки НСЗУ" та завантаженням даних із файлів.
 *
 * Наслідує функціонал від {@link AbstractController}, додаючи специфічну логіку для роботи з розшифровками НСЗУ.
 */
@Controller
@RequestMapping("/nszudecryption")
@Slf4j
public class NSZU_DecryptionController extends AbstractController<NszuDecryption, NszuDecryptionDTO> {

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param storageService сервіс для роботи із завантаженням файлів.
     * @param nszuDecryptionExcelReader зчитувач Excel-файлів для розшифровок НСЗУ.
     * @param nszu_decryptionService сервіс для роботи з розшифровками НСЗУ.
     * @param nszuDecryptionMapper маппер для перетворення між сутністю та DTO.
     */
    @Autowired
    public NSZU_DecryptionController(StorageService storageService,
                                     NszuDecryptionExcelReader nszuDecryptionExcelReader,
                                     NSZU_DecryptionService nszu_decryptionService,
                                     NSZU_DecryptionMapper nszuDecryptionMapper) {
        super("nszudecryption", storageService, nszuDecryptionExcelReader, nszu_decryptionService, nszuDecryptionMapper);
    }

    /**
     * Відображає сторінку розшифровок НСЗУ.
     *
     * Передає дані розшифровок у модель.
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва сторінки для відображення (назва сутності).
     */
    @GetMapping
    public String nszuDecryption(Model model) {
        log.info("Accessing nszuDecryption page");

        // Передає дані у модель
        sendDto(model);

        // Додає повідомлення про завантаження файлу
        model.addAttribute("msg_file", "file upload successfully");
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
     * Обробляє завантаження Excel-файлу з даними розшифровок НСЗУ.
     *
     * Зберігає дані з файлу у базу, якщо файл валідний, та додає повідомлення про результат завантаження.
     *
     * @param file завантажений файл.
     * @param monthYear період у форматі `yyyy-MM`, який використовується для іменування файлу.
     * @param redirectAttributes атрибути редиректу для передачі повідомлень.
     * @return перенаправлення на сторінку розшифровок НСЗУ.
     */
    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam String monthYear,
                                   RedirectAttributes redirectAttributes) {

        log.info("Accessing post files nszu_decryption page");

        // Обробка завантаження файлу, збереження даних у базу та передача результату через redirectAttributes
        saveEntitiesAndSendDtoAndErrors(file, monthYear, redirectAttributes);

        return "redirect:/" + entityName;
    }
}
