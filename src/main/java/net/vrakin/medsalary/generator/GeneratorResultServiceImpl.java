package net.vrakin.medsalary.generator;

import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.exception.CalculateTypeNotFoundException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.service.*;
import net.vrakin.medsalary.service.service_package_handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GeneratorResultServiceImpl implements GeneratorResultService {

    private static final Logger log = LoggerFactory.getLogger(GeneratorResultServiceImpl.class);

    public static final String EMPTY_SING = "0";
    private final StaffListRecordService staffListRecordService;

    private final ServicePackageService servicePackageService;

    private final NSZU_DecryptionService nszu_decryptionService;

    private final UserPositionService userPositionService;

    private final UserService userService;

    private final DepartmentService departmentService;

    private final CalculateManager calculateManager;

    @Autowired
    public GeneratorResultServiceImpl(StaffListRecordService staffListRecordService, ServicePackageService servicePackageService,
                                      NSZU_DecryptionService nszu_decryptionService, UserPositionService userPositionService,
                                      UserService userService, DepartmentService departmentService,
                                      CalculateManager calculateManager) {
        this.staffListRecordService = staffListRecordService;
        this.servicePackageService = servicePackageService;
        this.nszu_decryptionService = nszu_decryptionService;
        this.userPositionService = userPositionService;
        this.userService = userService;
        this.departmentService = departmentService;
        this.calculateManager = calculateManager;
    }

    @Override
    public Result generateResult(StaffListRecord staffListRecord) throws ResourceNotFoundException, CalculateTypeNotFoundException {

        Float hospSumPremium = 0f;
        Float sumAmblPremium = 0f;
        Float oneDaySurgerySumPremium = 0f;

        User user = staffListRecord.getUser();
        UserPosition userPosition = staffListRecord.getUserPosition();
        Department department = staffListRecord.getDepartment();

        Result result = getResult(user, userPosition, department, hospSumPremium, sumAmblPremium, oneDaySurgerySumPremium);

        if (Objects.requireNonNullElse(department.getServicePackages(), EMPTY_SING).equals(EMPTY_SING)
                || Objects.requireNonNullElse(userPosition.getServicePackageNumbers(), "0").equals(EMPTY_SING))
            return result;

        List<ServicePackage> servicePackageListByUserPositionAndDepartment;

        try {
            servicePackageListByUserPositionAndDepartment = generateListUserPositionDepartment(staffListRecord);
        }catch (NullPointerException e){
            log.error("error {}, department: {}", e.getMessage(), department.getName());
            return result;
        }
        if (servicePackageListByUserPositionAndDepartment.isEmpty()) {
            return result;
        }

        Float employmentPart = getEmploymentPart(staffListRecord, user);

        for (ServicePackage servicePackage: servicePackageListByUserPositionAndDepartment){
            switch (calculateManager.build(servicePackage)){
                case CalculateByStationaryNoOperation calculate:
                    hospSumPremium += calculate.calculate(servicePackage, userPosition,
                            getPlaceProvide(department),
                            employmentPart);
                    break;
                case CalculateByAmbulatoryNoOperation calculate:
                    sumAmblPremium += calculate.calculate(servicePackage, userPosition,
                            getPlaceProvide(department),
                            employmentPart);
                    break;
                case CalculateByOneDaySurgery calculate:
                    oneDaySurgerySumPremium += calculate.calculate(servicePackage, userPosition,
                            getPlaceProvide(department),
                            employmentPart);
                    break;
                case CalculateByPriorityServicePackage calculate:
                    sumAmblPremium += calculate.calculate(servicePackage, userPosition,
                            getPlaceProvide(department),
                            employmentPart);
                    break;
                default:
                    throw new CalculateTypeNotFoundException("Exception GeneratorResultService");
            }
        }

        result = getResult(user, userPosition, department, hospSumPremium, sumAmblPremium, oneDaySurgerySumPremium);
        log.info("result: {}", result.toString());
        return result;
    }

    private Float getEmploymentPart(StaffListRecord staffListRecord, User user) {
        Float employmentSum =
                staffListRecordService.findByUser(user)
                        .stream()
                        .map(u->u.getEmployment())
                        .reduce(0f, (u1, u2)-> u1 + u2);
        Float employmentPart = staffListRecord.getEmployment() / employmentSum;
        return employmentPart;
    }

    private List<ServicePackage> generateListUserPositionDepartment(StaffListRecord staffListRecord) {
        List<ServicePackage> servicePackageListByUserPositionAndDepartment;
        List<ServicePackage> servicePackages = servicePackageService.findByNumbers(
                Arrays.stream(staffListRecord.getUserPosition().getServicePackageNumbers().split(", ")).toList());
        List<String> servicePackageNumbersFromDepartment = Arrays.stream(staffListRecord.getDepartment().getServicePackages().split(", "))
                .toList();
        servicePackageListByUserPositionAndDepartment =
                servicePackages.stream().filter(s -> servicePackageNumbersFromDepartment.contains(s.getNumber())).collect(Collectors.toList());
        return servicePackageListByUserPositionAndDepartment;
    }

    private static Result getResult(User user, UserPosition userPosition, Department department, Float hospSumPremium, Float sumAmblPremium, Float oneDaySurgerySumPremium) {
        Result result = new Result();
        result.setUser(user);
        result.setUserPosition(userPosition);
        result.setDepartment(department);
        result.setHospNSZU_Premium(hospSumPremium);
        result.setAmblNSZU_Premium(sumAmblPremium);
        result.setOneDaySurgery(oneDaySurgerySumPremium);
        result.setDate(LocalDate.now());
        return result;
    }

    private float calculateHospPremiumByCount(long countNszuDecryption){
        return countNszuDecryption * 200f / 3;
    }

    private float calculateOneDaySugeryByCount(long countSurgery){
        return countSurgery * 160f;
    }

    private String getPlaceProvide(Department department){
        if (department.getDepartmentIsProId().startsWith("0175")){
            return "ЗАКАРПАТСЬКА область, МУКАЧІВСЬКИЙ район, місто МУКАЧЕВО, вулиця Грушевського Михайла, 29";
        }else {
            return "ЗАКАРПАТСЬКА область, МУКАЧІВСЬКИЙ район, місто МУКАЧЕВО, вулиця Новака Андрія, 8-13";
        }
    }
}
