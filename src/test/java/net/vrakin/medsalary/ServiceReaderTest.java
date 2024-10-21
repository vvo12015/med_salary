package net.vrakin.medsalary;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.config.InitData;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.excel.entity.reader.*;
import net.vrakin.medsalary.generator.GeneratorStaffListRecordService;
import net.vrakin.medsalary.service.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class ServiceReaderTest {

    public static final String USER_POSITION_FILENAME = "user_position";
    public static final String DEPARTMENT_FILENAME = "department";
    private static final int DEPARTMENT_COUNT = 8;
    private static final String STAFFLIST_FILENAME = "stafflist";
    private static final int STAFFLIST_COUNT = 10;
    public static final String NSZU_DECRYPTION_FILENAME = "nszu_decryption";
    private static final int NSZU_DECRYPTION_COUNT = 594;
    private static final int USER_POSITION_COUNT = 9;

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
    private ServicePackageService servicePackageService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private PremiumCategoryService premiumCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUserService securityUserService;

    public ServiceReaderTest() {
    }

    @BeforeAll
    public void setUp() throws IOException {
        initData.init();
    }

    @Test
    @Order(1)
    public void userPositionTest(){
        log.info("UserPosition loaded");

        String destinationFileName = String.format("%s_test%s", USER_POSITION_FILENAME, ExcelHelper.FILE_EXTENSION);

        List<UserPosition> userPositionList = userPositionExcelReader.readAllEntries(storageService.load(destinationFileName).toFile());

        userPositionService.saveAll(userPositionList);

        List<UserPosition> testUserPositionList = userPositionService.findAll();

        log.info("UserPositions size test");
        assertEquals(testUserPositionList.size(), USER_POSITION_COUNT);
    }

    @Test
    @Order(2)
    public void departmentTest(){
        log.info("Department loaded");

        String destinationFileName = String.format("%s_test%s", DEPARTMENT_FILENAME, ExcelHelper.FILE_EXTENSION);

        List<Department> departmentList = departmentExcelReader.readAllEntries(storageService.load(destinationFileName).toFile());

        departmentService.saveAll(departmentList);

        List<Department> testDepartmentList = departmentService.findAll();

        log.info("Department size test");
        assertEquals(testDepartmentList.size(), DEPARTMENT_COUNT);
    }

    @Test
    @Order(3)
    public void staffListTest() throws Exception {
        log.info("StaffList loaded");

        String destinationFileName = String.format("%s_test%s", STAFFLIST_FILENAME, ExcelHelper.FILE_EXTENSION);

        List<StaffListRecordDTO> staffListDTO = staffListRecordExcelReader.readAllDto(storageService.load(destinationFileName).toFile());
        List<StaffListRecord> staffList = new ArrayList<>();

        for (StaffListRecordDTO staffListRecordDTO : staffListDTO) {
            StaffListRecord staffListRecord = generatorStaffListRecordService.generate(staffListRecordDTO);
            staffList.add(staffListRecord);
        }
        staffListRecordService.saveAll(staffList);
        List<StaffListRecord> testStaffList = staffListRecordService.findAll();

        log.info("StaffList size test");
        assertEquals(testStaffList.size(), STAFFLIST_COUNT);
    }

    @Test
    @Order(4)
    public void nszuDecryptionTest(){
        log.info("NSZU Decryption loaded");

        String destinationFileName = String.format("%s_test%s", NSZU_DECRYPTION_FILENAME, ExcelHelper.FILE_EXTENSION);

        List<NszuDecryption> nszuDecryptionList = nszuDecryptionExcelReader.readAllEntries(storageService.load(destinationFileName).toFile());

        nszuDecryptionService.saveAll(nszuDecryptionList);

        List<NszuDecryption> testNszuDecryptionList = nszuDecryptionService.findAll();

        log.info("NszuDecryption size test");
        assertEquals(testNszuDecryptionList.size(), NSZU_DECRYPTION_COUNT);
    }

    @AfterAll
    public void tearDownAfterClass() throws Exception {
        resultService.deleteAll();
        nszuDecryptionService.deleteAll();
        staffListRecordService.deleteAll();
        servicePackageService.deleteAll();
        premiumCategoryService.deleteAll();
        userPositionService.deleteAll();
        departmentService.deleteAll();
        userService.deleteAll();
        securityUserService.deleteAll();
    }
}

