package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.excel.entity.reader.ExcelHelper;
import net.vrakin.medsalary.excel.entity.reader.ExcelReader;
import net.vrakin.medsalary.mapper.BaseMapper;
import net.vrakin.medsalary.service.Service;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Абстрактний контролер для роботи з ентитетами, які включають:
 * <ul>
 *     <li>Обробку завантаження файлів.</li>
 *     <li>Читання даних із файлів Excel та збереження їх у базу даних.</li>
 *     <li>Завантаження файлів із сервера.</li>
 *     <li>Передачу даних у моделі для відображення на сторінці.</li>
 * </ul>
 *
 * @param <E> тип сутності (Entity).
 * @param <D> тип об'єкта передачі даних (DTO).
 */
@Slf4j
public abstract class AbstractController<E, D> {

    /** Ім'я сутності, яке використовується для логування і передачі в модель. */
    protected final String entityName;

    /** Сервіс для роботи із завантаженням та зберіганням файлів. */
    protected final StorageService storageService;

    /** Зчитувач Excel-файлів, який читає дані та перетворює їх у DTO. */
    protected final ExcelReader<E, D> excelReader;

    /** Сервіс для роботи з базою даних. */
    protected final Service<E> service;

    /** Маппер для перетворення між сутностями та DTO. */
    protected final BaseMapper<E, D> mapper;

    /**
     * Конструктор для ініціалізації всіх залежностей.
     *
     * @param entityName ім'я сутності.
     * @param storageService сервіс для роботи з файлами.
     * @param excelReader зчитувач Excel-файлів.
     * @param service сервіс для роботи з базою даних.
     * @param mapper маппер для перетворення між сутністю та DTO.
     */
    public AbstractController(String entityName, StorageService storageService,
                              ExcelReader<E, D> excelReader, Service<E> service, BaseMapper<E, D> mapper) {
        this.entityName = entityName;
        this.storageService = storageService;
        this.excelReader = excelReader;
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Зберігає сутності в базу даних і додає DTO та повідомлення про помилки до атрибутів редиректу.
     *
     * @param file завантажений файл.
     * @param monthYear рядок у форматі `yyyy-MM`, який вказує період.
     * @param redirectAttributes атрибути редиректу для передачі повідомлень та даних.
     */
    protected void saveEntitiesAndSendDtoAndErrors(MultipartFile file, String monthYear, RedirectAttributes redirectAttributes) {
        YearMonth yearMonth = YearMonth.parse(monthYear);
        int monthNumber = yearMonth.getMonthValue();
        int yearNumber = yearMonth.getYear();

        LocalDate period = LocalDate.of(yearNumber, monthNumber, 1);

        String savedFileName = String.format("%s_%d_%02d" + ExcelHelper.FILE_EXTENSION, entityName, yearNumber, monthNumber);

        File destinationFile = storageService.storeUploadDir(file, savedFileName);

        String messageReadError = "";
        List<String> errorList = excelReader.isValidateFile(destinationFile);

        if (!errorList.isEmpty()) {
            messageReadError = errorList.stream().reduce((s, s2) -> s.concat(";\n Errors:\n" + s2)).toString();
        } else {
            log.info("Start save to DB");
            var DTOs = excelReader.readAllDto(destinationFile, period);
            var entities = mapper.toEntityList(DTOs);
            service.saveAll(entities);
            log.info("Finish save to DB");
            redirectAttributes.addFlashAttribute(entityName + "s", DTOs);
        }

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + savedFileName + "!" + messageReadError);
    }

    /**
     * Завантажує файл із сервера.
     *
     * @param filename ім'я файлу для завантаження.
     * @return HTTP-відповідь із файлом або статусом 404, якщо файл не знайдено.
     */
    protected ResponseEntity<Resource> getFile(String filename) {
        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * Передає DTO сутностей та список файлів у модель для відображення на сторінці.
     *
     * @param model модель для передачі атрибутів у шаблон.
     */
    protected void sendDto(Model model) {
        model.addAttribute("files", storageService.loadAll().map(
                        path -> path.getFileName().toString())
                .filter(f -> f.startsWith(entityName))
                .collect(Collectors.toList()));

        model.addAttribute(entityName + "s", mapper.toDtoList(service.findAll()));
    }

    /**
     * Генерує ім'я файлу для збереження та зберігає файл у директорію завантажень.
     *
     * @param file завантажений файл.
     * @param monthYear рядок у форматі `yyyy-MM`, який вказує період.
     * @return файл із повним шляхом до місця зберігання.
     */
    protected File getFileNameForSave(MultipartFile file, String monthYear) {
        YearMonth yearMonth = YearMonth.parse(monthYear);
        int monthNumber = yearMonth.getMonthValue();
        int yearNumber = yearMonth.getYear();
        String savedFileName = String.format("%s_%d_%02d" + ExcelHelper.FILE_EXTENSION, entityName, yearNumber, monthNumber);
        return storageService.storeUploadDir(file, savedFileName);
    }
}
