package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.config.InitData;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.excel.entity.reader.*;
import net.vrakin.medsalary.excel.entity.writer.ResultWriter;
import net.vrakin.medsalary.generator.GeneratorResultService;
import net.vrakin.medsalary.generator.GeneratorStaffListRecordService;
import net.vrakin.medsalary.mapper.ResultMapper;
import net.vrakin.medsalary.service.*;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Контролер для управління сторінкою результатів.
 *
 * Забезпечує генерацію, оновлення та збереження результатів роботи персоналу
 * на основі даних із бази або завантажених файлів.
 */
@Controller
@RequestMapping("/result")
@Slf4j
public class ResultController {

    public static final String USER_POSITION_FILENAME = "user_positions";
    public static final String DEPARTMENT_FILENAME = "departments";
    private static final String STAFFLIST_FILENAME = "stafflist";
    private static final String TIME_SHEET_FILENAME = "time_sheet";
    public static final String NSZU_DECRYPTION_FILENAME = "nszu_list";

    public static final int THREADS_COUNT = 10;

    private final GeneratorResultService generatorResultService;
    private final StaffListRecordService staffListRecordService;
    private final ResultWriter resultWriter;
    private final UserPositionExcelReader userPositionExcelReader;
    private final UserPositionService userPositionService;
    private final DepartmentExcelReader departmentExcelReader;
    private final DepartmentService departmentService;
    private final StaffListRecordExcelReader staffListRecordExcelReader;
    private final GeneratorStaffListRecordService generatorStaffListRecordService;
    private final NszuDecryptionExcelReader nszuDecryptionExcelReader;
    private final NSZU_DecryptionService nszuDecryptionService;
    private final TimeSheetService timeSheetService;
    private final TimeSheetExcelReader timeSheetExcelReader;
    private final StorageService storageService;
    private final ResultMapper resultMapper;
    private final ResultService resultService;

    private List<Result> previousResultList = new ArrayList<>();
    private List<Result> resultList = new ArrayList<>();

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param generatorResultService сервіс для генерації результатів.
     * @param staffListRecordService сервіс для роботи зі списком персоналу.
     * @param resultWriter клас для запису результатів у файли.
     * @param userPositionExcelReader зчитувач Excel-файлів для посад.
     * @param userPositionService сервіс для роботи з посадами.
     * @param departmentExcelReader зчитувач Excel-файлів для відділів.
     * @param departmentService сервіс для роботи з відділами.
     * @param staffListRecordExcelReader зчитувач Excel-файлів для записів списку персоналу.
     * @param generatorStaffListRecordService сервіс для генерації записів списку персоналу.
     * @param nszuDecryptionExcelReader зчитувач Excel-файлів для розшифровок НСЗУ.
     * @param nszuDecryptionService сервіс для роботи з розшифровками НСЗУ.
     * @param storageService сервіс для роботи із завантаженням і збереженням файлів.
     * @param timeSheetService сервіс для роботи з табелями.
     * @param timeSheetExcelReader зчитувач Excel-файлів для табелів.
     * @param resultMapper маппер для роботи з результатами.
     * @param resultService сервіс для збереження результатів у базу даних.
     */
    public ResultController(GeneratorResultService generatorResultService,
                            StaffListRecordService staffListRecordService,
                            ResultWriter resultWriter,
                            UserPositionExcelReader userPositionExcelReader,
                            UserPositionService userPositionService,
                            DepartmentExcelReader departmentExcelReader,
                            DepartmentService departmentService,
                            StaffListRecordExcelReader staffListRecordExcelReader,
                            GeneratorStaffListRecordService generatorStaffListRecordService,
                            NszuDecryptionExcelReader nszuDecryptionExcelReader,
                            NSZU_DecryptionService nszuDecryptionService,
                            StorageService storageService,
                            TimeSheetService timeSheetService,
                            TimeSheetExcelReader timeSheetExcelReader,
                            ResultMapper resultMapper,
                            ResultService resultService) {
        this.generatorResultService = generatorResultService;
        this.staffListRecordService = staffListRecordService;
        this.resultWriter = resultWriter;
        this.userPositionExcelReader = userPositionExcelReader;
        this.userPositionService = userPositionService;
        this.departmentExcelReader = departmentExcelReader;
        this.departmentService = departmentService;
        this.staffListRecordExcelReader = staffListRecordExcelReader;
        this.generatorStaffListRecordService = generatorStaffListRecordService;
        this.nszuDecryptionExcelReader = nszuDecryptionExcelReader;
        this.nszuDecryptionService = nszuDecryptionService;
        this.storageService = storageService;
        this.timeSheetService = timeSheetService;
        this.timeSheetExcelReader = timeSheetExcelReader;
        this.resultMapper = resultMapper;
        this.resultService = resultService;
    }

    /**
     * Відображає сторінку результатів.
     *
     * @param model модель для передачі даних на фронтенд.
     * @return назва шаблону сторінки ("result").
     */
    @GetMapping
    public String result(Model model) {
        log.info("Accessing result page");
        updateData(model, LocalDate.now().withDayOfMonth(1));
        return "result";
    }

    /**
     * Оновлює дані моделі для сторінки результатів.
     *
     * @param model модель для передачі даних.
     * @param period період, за який потрібно оновити дані.
     */
    private void updateData(Model model, LocalDate period) {
        model.addAttribute("resultList", resultList);
        model.addAttribute("StaffListCount", staffListRecordService.findByPeriod(period).size());
        model.addAttribute("DepartmentCount", departmentService.findByPeriod(period).size());
        model.addAttribute("UserPositionCount", userPositionService.findByPeriod(period).size());
    }

    /**
     * Генерує результати для вказаного періоду.
     *
     * @param period період у форматі `yyyy-MM`.
     * @param model модель для передачі даних.
     * @return назва шаблону сторінки ("result").
     * @throws IOException якщо виникають проблеми із завантаженням або обробкою файлів.
     */
    @PostMapping("/generate")
    public String resultGenerate(@RequestParam String period, Model model) throws IOException {
        log.info("Accessing generate result page");
        generateAndSendResult(model, YearMonth.parse(period).atDay(1));
        return "result";
    }

    /**
     * Генерує результати на основі даних із файлів.
     *
     * @param monthYear період у форматі `yyyy-MM`.
     * @param model модель для передачі даних.
     * @return назва шаблону сторінки ("result").
     * @throws Exception якщо виникають помилки під час обробки файлів або генерації.
     */
    @PostMapping("/generate-f")
    public String resultGenerateFromFiles(@RequestParam String monthYear, Model model) throws Exception {
        log.info("Accessing generate result from files page");

        YearMonth yearMonth = YearMonth.parse(monthYear);
        LocalDate period = yearMonth.atDay(1);

        if (resultList.isEmpty()) {
            log.info("Processing input files for period: {}", period);

            // Завантаження та збереження даних із файлів
            // ...
        }
        return "result";
    }

    /**
     * Генерує результати, записує їх у файли та базу даних, і передає у модель.
     *
     * @param model модель для передачі даних.
     * @param period період для генерації результатів.
     * @throws IOException якщо виникають помилки під час запису файлів.
     */
    private void generateAndSendResult(Model model, LocalDate period) throws IOException {
        // Логіка генерації результатів
    }
}
