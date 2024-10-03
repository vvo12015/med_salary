package net.vrakin.medsalary;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.config.InitData;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.excel.*;
import net.vrakin.medsalary.generator.GeneratorStaffListRecordService;
import net.vrakin.medsalary.repository.ServicePackageRepository;
import net.vrakin.medsalary.service.*;
import net.vrakin.medsalary.service.service_package_handler.CalculateManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalculateTest {

    public static final String USER_POSITION_FILENAME = "user_position";
    public static final String DEPARTMENT_FILENAME = "department";
    private static final String STAFFLIST_FILENAME = "stafflist";
    public static final String NSZU_DECRYPTION_FILENAME = "nszu_decryption";
    public static final String DOCTOR3_STAFFLIST_ID = "1882";
    public static final String SERVICE_PACKAGE_4_NUMBER = "4";
    public static final String DOCTOR2_STAFFLIST_ID = "1454";
    public static final String SERVICE_PACKAGE_9_NUMBER = "9";
    public static final String DOCTOR1_STAFFLIST_ID = "532";
    public static final String SERVICE_PACKAGE_47_NUMBER = "47";
    public static final String DOCTOR4_STAFFLIST_ID = "622";
    public static final String SERVICE_PACKAGE_12_NUMBER = "12";
    public static final String SERVICE_PACKAGE_13_NUMBER = "13";


    @Autowired
    private UserPositionExcelReader userPositionExcelReader;

    @Autowired
    private UserPositionService userPositionService;

    @Autowired
    private DepartmentExcelReader departmentExcelReader;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private StaffListRecordExcelReader staffListRecordExcelReader;

    @Autowired
    private StaffListRecordService staffListRecordService;

    @Autowired
    private GeneratorStaffListRecordService generatorStaffListRecordService;

    @Autowired
    private NszuDecryptionExcelReader nszuDecryptionExcelReader;


    @Autowired
    private NSZU_DecryptionService nszuDecryptionService;

    @Autowired
    private InitData initData;

    @Autowired
    private StorageService storageService;

    @Autowired
    private CalculateManager calculateManager;

    @Autowired
    private ServicePackageService servicePackageService;

    public CalculateTest() {
    }

    @BeforeAll
    public void setUp() throws IOException {
        initData.init();
        String userPositionFile = String.format("%s_test%s", USER_POSITION_FILENAME, ExcelHelper.FILE_EXTENSION);
        List<UserPosition> userPositionList = userPositionExcelReader.readAllEntries(storageService.load(userPositionFile).toFile());
        userPositionService.saveAll(userPositionList);

        String departmentFileName = String.format("%s_test%s", DEPARTMENT_FILENAME, ExcelHelper.FILE_EXTENSION);
        List<Department> departmentList = departmentExcelReader.readAllEntries(storageService.load(departmentFileName).toFile());
        departmentService.saveAll(departmentList);

        String staffListFileName = String.format("%s_test%s", STAFFLIST_FILENAME, ExcelHelper.FILE_EXTENSION);

        List<StaffListRecordDTO> staffListDTO = staffListRecordExcelReader.readAllDto(storageService.load(staffListFileName).toFile());
        List<StaffListRecord> staffList = new ArrayList<>();

        for (StaffListRecordDTO staffListRecordDTO : staffListDTO) {
            StaffListRecord staffListRecord = generatorStaffListRecordService.generate(staffListRecordDTO);
            staffList.add(staffListRecord);
        }
        staffListRecordService.saveAll(staffList);

        String nszuDecryptionName = String.format("%s_test%s", NSZU_DECRYPTION_FILENAME, ExcelHelper.FILE_EXTENSION);

        List<NszuDecryption> nszuDecryptionList = nszuDecryptionExcelReader.readAllEntries(storageService.load(nszuDecryptionName).toFile());

        nszuDecryptionService.saveAll(nszuDecryptionList);
    }

    @Test
    public void calculateStationaryNoOperationTest(){

        StaffListRecord staffListRecordDoctor3 = staffListRecordService.findByStaffListId(DOCTOR3_STAFFLIST_ID).get();
        ServicePackage servicePackage4 = servicePackageService.findByNumber(SERVICE_PACKAGE_4_NUMBER).get();

        Result result = new Result(staffListRecordDoctor3.getUser(), staffListRecordDoctor3.getUserPosition(),
                staffListRecordDoctor3.getDepartment(), 1f);

        calculateManager.calculate(servicePackage4, result);

        assertEquals(result.getHospNSZU_Premium(), 200);
    }

    @Test
    public void calculateAmbulatoryNoOperationTest(){

        StaffListRecord staffListRecordDoctor2 = staffListRecordService.findByStaffListId(DOCTOR2_STAFFLIST_ID).get();
        ServicePackage servicePackage9 = servicePackageService.findByNumber(SERVICE_PACKAGE_9_NUMBER).get();

        Result result = new Result(staffListRecordDoctor2.getUser(), staffListRecordDoctor2.getUserPosition(),
                staffListRecordDoctor2.getDepartment(), 1f);

        calculateManager.calculate(servicePackage9, result);

        assertEquals(result.getAmblNSZU_Premium(), 7282.9f);
    }

    @Test
    public void calculateOneDaySurgeryTest(){

        StaffListRecord staffListRecordDoctor1 = staffListRecordService.findByStaffListId(DOCTOR1_STAFFLIST_ID).get();
        ServicePackage servicePackage47 = servicePackageService.findByNumber(SERVICE_PACKAGE_47_NUMBER).get();

        Result result = new Result(staffListRecordDoctor1.getUser(), staffListRecordDoctor1.getUserPosition(),
                staffListRecordDoctor1.getDepartment(), 1f);

        calculateManager.calculate(servicePackage47, result);

        assertEquals(result.getOneDaySurgery(), 1760f);
    }

    @Test
    public void calculatePriorityServicePackageTest(){

        StaffListRecord staffListRecordDoctor4 = staffListRecordService.findByStaffListId(DOCTOR4_STAFFLIST_ID).get();
        ServicePackage servicePackage12 = servicePackageService.findByNumber(SERVICE_PACKAGE_12_NUMBER).get();
        ServicePackage servicePackage13 = servicePackageService.findByNumber(SERVICE_PACKAGE_13_NUMBER).get();

        Result result = new Result(staffListRecordDoctor4.getUser(), staffListRecordDoctor4.getUserPosition(),
                staffListRecordDoctor4.getDepartment(), 1f);

        calculateManager.calculate(servicePackage12, result);
        calculateManager.calculate(servicePackage13, result);

        assertEquals(result.getAmblNSZU_Premium(), 160f);
    }
}
