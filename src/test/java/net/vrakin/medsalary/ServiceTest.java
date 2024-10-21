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
public final class ServiceTest {

    public static final String USER_POSITION_FILENAME = "user_position";
    public static final String DEPARTMENT_FILENAME = "department";
    private static final String STAFFLIST_FILENAME = "stafflist";
    public static final String NSZU_DECRYPTION_FILENAME = "nszu_decryption";
    public static final String SERVICE_PACKAGE_NUMBER_4_NAME = "4 Стаціонарна допомога дорослим та дітям без проведення хірургічних операцій";
    public static final String SERVICE_PACKAGE_NUMBER_47_NAME = "47 Хірургічні операції дорослим та дітям в умовах стаціонару одного дня";
    public static final String SERVICE_PACKAGE_NUMBER_9_NAME = "9 Профілактика, діагностика, спостереження та лікування в амбулаторних умовах";
    public static final String SERVICE_PACKAGE_NUMBER_12_NAME = "12 Езофагогастродуоденоскопія";
    public static final String SERVICE_PACKAGE_NUMBER_13_NAME = "13 Колоноскопія";
    public static final String SERVICE_PACKAGE_NUMBER_10_NAME = "10 Мамографія";
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

    @Autowired
    private ResultService resultService;

    @Autowired
    private PremiumCategoryService premiumCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUserService securityUserService;

    public ServiceTest() {
    }

    @BeforeAll
    public void setUp() throws Exception {
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
    public void findByUserServicePackageCountTest(){
        log.info("findAll: {}", nszuDecryptionService.findAll().size());
        assertEquals(nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
            "Doctor3", "up4").size(),3);
        assertEquals(nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                "Doctor1", "up1").size(),11);
        assertEquals(nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                "Doctor2", "up2").size(),351);
        assertEquals(nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                "Doctor4", "up5").size(),6);
        assertEquals(nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                "Doctor7", "up8").size(),4);
        assertEquals(nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                "Doctor5", "up6").size(),46);
        assertEquals(nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                "Doctor6", "up7").size(),173);
    }

    @Test
    public void findByUserServicePackageSumTest(){

        nszuDecryptionService.findByExecutorName("Doctor4")
                .forEach(nszuDecryption -> log.info("Doctor4, servicePackageName: {}", nszuDecryption.getExecutorUserPosition()));
        Float sumDoctor3Up4 =
        nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                "Doctor3", "up4").stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                                .reduce(0f, Float::sum);
        float actualDoctor3Up4 = 16266.76f;

        log.info("sum: {}, actual: {}", sumDoctor3Up4, actualDoctor3Up4);
        assertEquals(sumDoctor3Up4,16266.76f);

        Float sumDoctor1Up1 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                                "Doctor1", "up1").stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);

        float actualDoctor1Up1 = 49876f;
        log.info("sum: {}, actual: {}", sumDoctor1Up1, actualDoctor1Up1);

        assertEquals(sumDoctor1Up1, actualDoctor1Up1);

        Float sumDoctor2Up2 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                                "Doctor2", "up2").stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);
        float actualDoctor2Up2 = 62829f;
        log.info("sum: {}, actual: {}", sumDoctor2Up2, actualDoctor2Up2);

        assertEquals(sumDoctor2Up2, 62829f);

        Float sumDoctor4Up5 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                                "Doctor4", "up5").stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);

        float actualDoctor4Up5 = 6750f;
        log.info("sum: {}, actual: {}", sumDoctor4Up5, actualDoctor4Up5);

        assertEquals(sumDoctor4Up5, actualDoctor4Up5);

        Float sumDoctor7Up8 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                                "Doctor7", "up8").stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);
        assertEquals(sumDoctor7Up8, 960f);

        Float sumDoctor5Up6 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                                "Doctor5", "up6").stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);
        assertEquals(sumDoctor5Up6, 2576f);

        Float sumDoctor6Up7 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(
                                "Doctor6", "up7").stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);
        assertEquals(sumDoctor6Up7, 30600f);
    }

    @Test
    public void findByExecutorNameAndExecutorUserPositionAndServicePackageNameSumTest(){
        Float sumDoctor3ServicePackage4Up4 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPositionAndServicePackageName(
                                "Doctor3", "up4", SERVICE_PACKAGE_NUMBER_4_NAME).stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);
        assertEquals(sumDoctor3ServicePackage4Up4, 16266.76f);

        Float sumDoctor1ServicePackage47Up1 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPositionAndServicePackageName(
                                "Doctor1", "up1", SERVICE_PACKAGE_NUMBER_47_NAME
                        ).stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);
        assertEquals(sumDoctor1ServicePackage47Up1, 49876f);

        Float sumDoctor2ServicePackage9Up2 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPositionAndServicePackageName(
                                "Doctor2", "up2",
                                SERVICE_PACKAGE_NUMBER_9_NAME
                        ).stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);
        assertEquals(sumDoctor2ServicePackage9Up2, 62829f);

        Float sumDoctor4ServicePackage12Up5=
                nszuDecryptionService.findByExecutorNameAndExecutorUserPositionAndServicePackageName(
                                "Doctor4", "up5",
                                SERVICE_PACKAGE_NUMBER_12_NAME
                        ).stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);
        assertEquals(sumDoctor4ServicePackage12Up5, 2643f);

        Float sumDoctor4ServicePackage13Up5 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPositionAndServicePackageName(
                                "Doctor4", "up5",
                                SERVICE_PACKAGE_NUMBER_13_NAME
                        ).stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);
        assertEquals(sumDoctor4ServicePackage13Up5, 4107f);

        Float sumDoctor7ServicePackage10Up8 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPositionAndServicePackageName(
                                "Doctor7", "up8",
                                SERVICE_PACKAGE_NUMBER_10_NAME
                        ).stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);
        assertEquals(sumDoctor7ServicePackage10Up8, 960f);

        Float sumDoctor5ServicePackage9Up6 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPositionAndServicePackageName(
                                "Doctor5", "up6",
                                SERVICE_PACKAGE_NUMBER_9_NAME
                        ).stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);

        float actualDoctor5ServicePackage9Up6 = 2576f;

        log.info("sum: {}, actual: {}", sumDoctor5ServicePackage9Up6, actualDoctor5ServicePackage9Up6);

        assertEquals(sumDoctor5ServicePackage9Up6, 2576f);

        Float sumDoctor6ServicePackage9Up7 =
                nszuDecryptionService.findByExecutorNameAndExecutorUserPositionAndServicePackageName(
                                "Doctor6", "up7",
                                SERVICE_PACKAGE_NUMBER_9_NAME
                        ).stream()
                        .map(n->Float.parseFloat(n.getPaymentFact()))
                        .reduce(0f, Float::sum);

        float actualDoctor6ServicePackage9Up7 = 30600f;

        log.info("sum: {}, actual: {}", sumDoctor6ServicePackage9Up7, actualDoctor6ServicePackage9Up7);

        assertEquals(sumDoctor6ServicePackage9Up7, 30600f);
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

