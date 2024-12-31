package net.vrakin.medsalary.controller;

import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.dto.*;
import net.vrakin.medsalary.excel.entity.reader.ExcelReader;
import net.vrakin.medsalary.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Контролер для модуля "Єдине вікно".
 *
 * Забезпечує функціонал для завантаження файлів із різними типами даних
 * (список персоналу, посади, відділи, розшифровки НСЗУ, табелі тощо)
 * та автоматичного їх збереження у базу даних.
 */
@Controller
@RequestMapping("/one-window")
public class OneWindowsController {

    private final ExcelReader<StaffListRecord, StaffListRecordDTO> staffListExcelReader;
    private final ExcelReader<UserPosition, UserPositionDTO> userPositionExcelReader;
    private final ExcelReader<Department, DepartmentDTO> departmentExcelReader;
    private final ExcelReader<NszuDecryption, NszuDecryptionDTO> nszuDecryptionExcelReader;
    private final ExcelReader<TimeSheet, TimeSheetDTO> timeSheetExcelReader;
    private final ExcelReader<ServicePackage, ServicePackageDTO> servicePackageExcelReader;

    private final StaffListRecordService staffListRecordService;
    private final UserPositionService userPositionService;
    private final DepartmentService departmentService;
    private final NSZU_DecryptionService nszu_decryptionService;
    private final TimeSheetService timeSheetService;
    private final StorageService storageService;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param staffListExcelReader зчитувач Excel-файлів для списку персоналу.
     * @param userPositionExcelReader зчитувач Excel-файлів для посад.
     * @param departmentExcelReader зчитувач Excel-файлів для відділів.
     * @param nszuDecryptionExcelReader зчитувач Excel-файлів для розшифровок НСЗУ.
     * @param timeSheetExcelReader зчитувач Excel-файлів для табелів.
     * @param servicePackageExcelReader зчитувач Excel-файлів для пакетів послуг.
     * @param staffListRecordService сервіс для роботи зі списком персоналу.
     * @param nszu_decryptionService сервіс для роботи з розшифровками НСЗУ.
     * @param storageService сервіс для роботи із завантаженням файлів.
     */
    @Autowired
    public OneWindowsController(ExcelReader<StaffListRecord, StaffListRecordDTO> staffListExcelReader,
                                ExcelReader<UserPosition, UserPositionDTO> userPositionExcelReader,
                                ExcelReader<Department, DepartmentDTO> departmentExcelReader,
                                ExcelReader<NszuDecryption, NszuDecryptionDTO> nszuDecryptionExcelReader,
                                ExcelReader<TimeSheet, TimeSheetDTO> timeSheetExcelReader,
                                ExcelReader<ServicePackage, ServicePackageDTO> servicePackageExcelReader,
                                StaffListRecordService staffListRecordService,
                                NSZU_DecryptionService nszu_decryptionService,
                                UserPositionService userPositionService,
                                DepartmentService departmentService,
                                TimeSheetService timeSheetService,
                                StorageService storageService) {
        this.staffListExcelReader = staffListExcelReader;
        this.userPositionExcelReader = userPositionExcelReader;
        this.departmentExcelReader = departmentExcelReader;
        this.nszuDecryptionExcelReader = nszuDecryptionExcelReader;
        this.timeSheetExcelReader = timeSheetExcelReader;
        this.servicePackageExcelReader = servicePackageExcelReader;
        this.staffListRecordService = staffListRecordService;
        this.nszu_decryptionService = nszu_decryptionService;
        this.storageService = storageService;
        this.userPositionService = userPositionService;
        this.departmentService = departmentService;
        this.timeSheetService = timeSheetService;
    }

    /**
     * Відображає сторінку "Єдине вікно".
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва шаблону сторінки ("oneWindow").
     */
    @GetMapping
    public String oneWindowPage(Model model) {
        return "oneWindow";
    }

    /**
     * Обробляє завантаження файлів для різних типів даних.
     *
     * Виконує перевірку файлів на відповідність типам даних (список персоналу, посади, відділи, розшифровки НСЗУ, табелі тощо)
     * та зберігає їх у базу даних. У разі помилок додає повідомлення до атрибутів редиректу.
     *
     * @param files список завантажених файлів.
     * @param monthYear період у форматі `yyyy-MM`, який використовується для визначення періоду.
     * @param redirectAttributes атрибути редиректу для передачі повідомлень.
     * @return перенаправлення на сторінку "Єдине вікно".
     * @throws IOException якщо виникає помилка під час зчитування файлів.
     */
    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("files") List<MultipartFile> files,
                                   @RequestParam String monthYear,
                                   RedirectAttributes redirectAttributes) throws IOException {

        List<String> messageErrors = new ArrayList<>();
        List<File> distinationFiles = new ArrayList<>();

        // Завантаження файлів із робочої директорії
        for (MultipartFile file : files) {
            distinationFiles.add(storageService.loadFromWorkDir(file.getName()).toFile());
        }

        LocalDate datePeriod = LocalDate.parse(monthYear + "-01");

        // Перевірка та збереження файлів для кожного типу даних
        for (File file : distinationFiles) {

            if (staffListExcelReader.isValidateFile(file).isEmpty()) {
                staffListRecordService.saveAll(staffListExcelReader.readAllEntries(file, datePeriod));
                continue;
            }

            if (userPositionExcelReader.isValidateFile(file).isEmpty()) {
                userPositionService.saveAll(userPositionExcelReader.readAllEntries(file, datePeriod));
                continue;
            }

            if (departmentExcelReader.isValidateFile(file).isEmpty()) {
                departmentService.saveAll(departmentExcelReader.readAllEntries(file, datePeriod));
                continue;
            }

            if (nszuDecryptionExcelReader.isValidateFile(file).isEmpty()) {
                nszu_decryptionService.saveAll(nszuDecryptionExcelReader.readAllEntries(file, datePeriod));
                continue;
            }

            if (timeSheetExcelReader.isValidateFile(file).isEmpty()) {
                timeSheetService.saveAll(timeSheetExcelReader.readAllEntries(file, datePeriod));
                continue;
            }

            // Додати повідомлення про помилку, якщо файл не відповідає жодному типу
            messageErrors.add(String.format("Error with file %s", file.getName()));
        }

        redirectAttributes.addAttribute("messageErrors", messageErrors);

        return "redirect:/one-window";
    }
}
