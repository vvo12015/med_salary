package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.config.InitData;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.excel.entity.reader.*;
import net.vrakin.medsalary.generator.GeneratorResultService;
import net.vrakin.medsalary.generator.GeneratorStaffListRecordService;
import net.vrakin.medsalary.service.*;
import net.vrakin.medsalary.service.service_package_handler.CalculateManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
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
    private final ResultExcelWriter resultExcelWriter;

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

    private final InitData initData;

    private final StorageService storageService;

    public ResultController(GeneratorResultService generatorResultService,
                            StaffListRecordService staffListRecordService,
                            ResultExcelWriter resultExcelWriter,
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
                            TimeSheetExcelReader timeSheetExcelReader) {
        this.generatorResultService = generatorResultService;
        this.staffListRecordService = staffListRecordService;
        this.resultExcelWriter = resultExcelWriter;
        this.userPositionExcelReader = userPositionExcelReader;
        this.userPositionService = userPositionService;
        this.departmentExcelReader = departmentExcelReader;
        this.departmentService = departmentService;
        this.staffListRecordExcelReader = staffListRecordExcelReader;
        this.generatorStaffListRecordService = generatorStaffListRecordService;
        this.nszuDecryptionExcelReader = nszuDecryptionExcelReader;
        this.nszuDecryptionService = nszuDecryptionService;
        this.initData = initData;
        this.storageService = storageService;
        this.timeSheetService = timeSheetService;
        this.timeSheetExcelReader = timeSheetExcelReader;
    }

    private List<Result> resultList = new ArrayList<>();

    @GetMapping
    public String result(Model model){
        log.info("Accessing result page");

        model.addAttribute("resultList", resultList);

        return "result";
    }

    @GetMapping("/generate")
    public String resultGenerate(Model model) throws IOException {
        log.info("Accessing generate result page");

        generateAndSendResult(model);

        return "result";
    }

    @GetMapping("/generate-f")
    public String resultGenerateFromFiles(Model model) throws Exception {
        log.info("Accessing generate result from files page");

        log.info("resultList.size: {}", resultList.size());

        if (resultList.isEmpty()) {
            String userPositionFile = String.format("%s%s", USER_POSITION_FILENAME, ExcelHelper.FILE_EXTENSION);
            log.info("Loading userPositions from file");
            List<UserPosition> userPositionList = userPositionExcelReader.readAllEntries(storageService.loadFromWorkDir(userPositionFile).toFile());

            log.info("Saving userPositions");
            userPositionService.saveAll(userPositionList);

            String departmentFileName = String.format("%s%s", DEPARTMENT_FILENAME, ExcelHelper.FILE_EXTENSION);
            log.info("Loading department from file");
            List<Department> departmentList = departmentExcelReader.readAllEntries(storageService.loadFromWorkDir(departmentFileName).toFile());
            log.info("Saving department");
            departmentService.saveAll(departmentList);

            String timeSheetFileName = String.format("%s%s", TIME_SHEET_FILENAME, ExcelHelper.FILE_EXTENSION);
            log.info("Loading time sheet from file");
            List<TimeSheet> timeSheets = timeSheetExcelReader.readAllEntries(storageService.loadFromWorkDir(timeSheetFileName).toFile());
            log.info("Saving timeSheets");
            timeSheetService.saveAll(timeSheets);

            String staffListFileName = String.format("%s%s", STAFFLIST_FILENAME, ExcelHelper.FILE_EXTENSION);

            log.info("Loading staffList from file");
            List<StaffListRecordDTO> staffListDTO = staffListRecordExcelReader.readAllDto(storageService.loadFromWorkDir(staffListFileName).toFile());
            List<StaffListRecord> staffList = new ArrayList<>();

            log.info("Generating stafflist start");
            for (StaffListRecordDTO staffListRecordDTO : staffListDTO) {
                if (staffListRecordService.findByStaffListId(staffListRecordDTO.getStaffListId()).isEmpty()) {
                    StaffListRecord staffListRecord = generatorStaffListRecordService.generate(staffListRecordDTO);
                    staffList.add(staffListRecord);
                }
            }
            log.info("Saving staffList");
            staffListRecordService.saveAll(staffList);

            String nszuDecryptionName = String.format("%s%s", NSZU_DECRYPTION_FILENAME, ExcelHelper.FILE_EXTENSION);

            log.info("Loading NSZU_Decryption from file");
            List<NszuDecryption> nszuDecryptionList = nszuDecryptionExcelReader.readAllEntries(storageService.loadFromWorkDir(nszuDecryptionName).toFile());

            log.info("Saving NSZU_Decryption");
            if (nszuDecryptionService.findAll().size() < 5) nszuDecryptionService.saveAll(nszuDecryptionList);

            generateAndSendResult(model);
        }
        return "result";
    }

    private void generateAndSendResult(Model model) throws IOException {
        log.info("StaffList find start");
        List<StaffListRecord> staffListRecordList = staffListRecordService.findAll();

        log.info("Result generating start");
        resultList = staffListRecordList.stream().map(s -> {
                    try {
                        return generatorResultService.generate(s);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        log.info("Result write in file");
        resultExcelWriter.writeAll(resultList);

        model.addAttribute("result_count", resultList.size());
        model.addAttribute("results", resultList);
    }
}
