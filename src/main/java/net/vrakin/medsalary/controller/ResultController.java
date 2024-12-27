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
                            InitData initData, StorageService storageService,
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

    private List<Result> previousResultList = new ArrayList<>();
    private List<Result> resultList = new ArrayList<>();

    @GetMapping
    public String result(Model model){
        log.info("Accessing result page");

        updateData(model, LocalDate.now().withDayOfMonth(1));

        return "result";
    }

    private void updateData(Model model, LocalDate period) {
        model.addAttribute("resultList", resultList);
        model.addAttribute("StaffListCount", staffListRecordService.findByPeriod(period).size());
        model.addAttribute("DepartmentCount", departmentService.findByPeriod(period).size());
        model.addAttribute("UserPositionCount", userPositionService.findByPeriod(period).size());
    }

    @GetMapping("/update")
    public String updateForPeriod(@RequestParam String period, Model model) throws IOException {
        log.info("Accessing generate result page");

        updateData(model, YearMonth.parse(period).atDay(01));

        return "result";
    }

    @PostMapping("/generate")
    public String resultGenerate(@RequestParam String period, Model model) throws IOException {
        log.info("Accessing generate result page");

        generateAndSendResult(model, YearMonth.parse(period).atDay(01));

        return "result";
    }

    @PostMapping("/generate-f")
    public String resultGenerateFromFiles(@RequestParam String monthYear, Model model) throws Exception {
        log.info("Accessing generate result from files page");

        YearMonth yearMonth = YearMonth.parse(monthYear);
        LocalDate period = yearMonth.atDay(1);

        log.info("resultList.size: {}", resultList.size());

        if (resultList.isEmpty()) {
            String userPositionFile = String.format("%s%s", USER_POSITION_FILENAME, ExcelHelper.FILE_EXTENSION);
            log.info("Loading userPositions from file");
            List<UserPosition> userPositionList = userPositionExcelReader.readAllEntries(storageService.loadFromWorkDir(userPositionFile).toFile(), period);

            log.info("Saving userPositions");
            userPositionService.saveAll(userPositionList);

            String departmentFileName = String.format("%s%s", DEPARTMENT_FILENAME, ExcelHelper.FILE_EXTENSION);
            log.info("Loading department from file");
            List<Department> departmentList = departmentExcelReader.readAllEntries(storageService.loadFromWorkDir(departmentFileName).toFile(), period);
            log.info("Saving department");
            departmentService.saveAll(departmentList);

            String timeSheetFileName = String.format("%s%s", TIME_SHEET_FILENAME, ExcelHelper.FILE_EXTENSION);
            log.info("Loading time sheet from file");
            List<TimeSheet> timeSheets = timeSheetExcelReader.readAllEntries(storageService.loadFromWorkDir(timeSheetFileName).toFile(), period);
            log.info("Saving timeSheets");
            timeSheetService.saveAll(timeSheets);

            String staffListFileName = String.format("%s%s", STAFFLIST_FILENAME, ExcelHelper.FILE_EXTENSION);

            log.info("Loading staffList from file");
            List<StaffListRecordDTO> staffListDTOs = staffListRecordExcelReader.readAllDto(storageService.loadFromWorkDir(staffListFileName).toFile(), period);
            List<StaffListRecord> staffList = new ArrayList<>();

            log.info("Generating stafflist start");
            for (StaffListRecordDTO staffListRecordDTO : staffListDTOs) {
                if (staffListRecordService.findByStaffListId(staffListRecordDTO.getStaffListId()).isEmpty()) {
                    StaffListRecord staffListRecord = generatorStaffListRecordService.generate(staffListRecordDTO);
                    staffList.add(staffListRecord);
                }
            }
            log.info("Saving staffList");
            staffListRecordService.saveAll(staffList);

            String nszuDecryptionName = String.format("%s%s", NSZU_DECRYPTION_FILENAME, ExcelHelper.FILE_EXTENSION);

            log.info("Loading NSZU_Decryption from file");
            List<NszuDecryption> nszuDecryptionList = nszuDecryptionExcelReader.readAllEntries(storageService.loadFromWorkDir(nszuDecryptionName).toFile(), period);

            log.info("Saving NSZU_Decryption");
            if (nszuDecryptionService.findAll().size() < 5) nszuDecryptionService.saveAll(nszuDecryptionList);

            log.info("Period: {}", period);
            generateAndSendResult(model, period);
        }
        return "result";
    }



    private void generateAndSendResult(Model model, LocalDate period) throws IOException {
        log.info("StaffList find start");

        LocalDate previousPeriod = period.minusMonths(1);

       /* int offset = 20; // Кількість записів, які потрібно пропустити
        int limit = 2;*/ // Кількість записів, які потрібно отримати

        List<StaffListRecord> previousStaffListRecordList = staffListRecordService.findByPeriod(previousPeriod).stream()
                //.filter(s->s.getUserPosition().getName().startsWith("лікар"))
                .sorted(Comparator.comparing(s -> s.getUser().getName()))
                /*.limit(offset)
                .limit(limit)*/
                .toList();

        log.info("Result generating start");
        previousResultList = previousStaffListRecordList.stream().map(s -> {
                    try {
                        return generatorResultService.generate(s);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();


        log.info("Result write in file");

        try {
            resultService.saveAll(previousResultList);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        List<StaffListRecord> staffListRecordList = staffListRecordService.findByPeriod(period).stream()
                //.filter(s->s.getUserPosition().getName().startsWith("лікар"))
                .sorted(Comparator.comparing(s -> s.getUser().getName()))
                /*.limit(offset)
                .limit(limit)*/
                .toList();

        log.info("Previous result generating start");
        resultList = staffListRecordList.stream().map(s -> {
            try {
                // Генерація поточного результату
                Result result = generatorResultService.generate(s);

                List<StaffListRecord> firstRecordList = previousStaffListRecordList.stream()
                        .filter(pSt -> pSt.getStaffListId().equals(s.getStaffListId()))
                        .toList();

                if (firstRecordList.size() == 1) {
                    StaffListRecord previousStaffListRecord = firstRecordList.stream().findFirst().get();

                    // Пошук попередніх результатів
                    List<Result> previousResults = resultService.findByStaffListRecordAndDate(previousStaffListRecord, previousPeriod);

                    // Агрегація числових значень через Stream
                    int totalCountEMR_stationary = previousResults.stream()
                            .mapToInt(Result::getCountEMR_stationary)
                            .sum() + result.getCountEMR_stationary();

                    int totalCountEMR_ambulatory = previousResults.stream()
                            .mapToInt(Result::getCountEMR_ambulatory)
                            .sum() + result.getCountEMR_ambulatory();

                    int totalCountEMR_oneDaySurgery = previousResults.stream()
                            .mapToInt(Result::getCountEMR_oneDaySurgery)
                            .sum() + result.getCountEMR_oneDaySurgery();

                    int totalCountEMR_priorityService = previousResults.stream()
                            .mapToInt(Result::getCountEMR_priorityService)
                            .sum() + result.getCountEMR_priorityService();

                    float totalSumForAmlPackage = (float) previousResults.stream()
                            .mapToDouble(Result::getSumForAmlPackage)
                            .sum() + result.getSumForAmlPackage();

                    float totalHospNSZU_Premium = (float) previousResults.stream()
                            .mapToDouble(Result::getHospNSZU_Premium)
                            .sum() + result.getHospNSZU_Premium();

                    float totalAmblNSZU_Premium = (float) previousResults.stream()
                            .mapToDouble(Result::getAmblNSZU_Premium)
                            .sum() + result.getAmblNSZU_Premium();

                    float totalOneDaySurgery = (float) previousResults.stream()
                            .mapToDouble(Result::getOneDaySurgeryPremium)
                            .sum() + result.getOneDaySurgeryPremium();

                    // Оновлення полів поточного результату
                    result.setCountEMR_stationary(totalCountEMR_stationary);
                    result.setCountEMR_ambulatory(totalCountEMR_ambulatory);
                    result.setCountEMR_oneDaySurgery(totalCountEMR_oneDaySurgery);
                    result.setCountEMR_priorityService(totalCountEMR_priorityService);

                    result.setSumForAmlPackage(totalSumForAmlPackage);
                    result.setHospNSZU_Premium(totalHospNSZU_Premium);
                    result.setAmblNSZU_Premium(totalAmblNSZU_Premium);
                    result.setOneDaySurgeryPremium(totalOneDaySurgery);
                } else if (firstRecordList.size() > 1) {
                    throw new BadRequestException(String.format("Duplicate staffListRecord: %s", s.getStaffListId()));
                }else{
                    log.warn("This staffListRecord not found: {}", s.toString());
                }
                return result; // Додати оновлений результат до списку
            } catch (Exception e) {
                throw new RuntimeException("Error processing record for " + s, e);
            }
        }).toList();

        log.info("Result write in excel file");
        for (Result result :
                resultList) {
            log.info(result.toString());
        }
        resultWriter.writeAll(resultList);
        log.info("Result write in sql file");
        resultWriter.writeAllToSQL(resultList);
        log.info("Result write in db");
//        resultService.saveAll(resultList);

        model.addAttribute("colNames", resultMapper.toStringListColNameForExcel());
        model.addAttribute("result_count", resultList.size());
        model.addAttribute("results", resultList.stream().map(resultMapper::toStringList).toList());
    }
}
