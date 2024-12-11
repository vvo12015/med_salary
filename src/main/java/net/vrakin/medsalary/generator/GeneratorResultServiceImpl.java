package net.vrakin.medsalary.generator;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.service.PremiumCategoryService;
import net.vrakin.medsalary.service.ServicePackageService;
import net.vrakin.medsalary.service.StaffListRecordService;
import net.vrakin.medsalary.service.TimeSheetService;
import net.vrakin.medsalary.service.service_package_handler.CalculateManager;
import net.vrakin.medsalary.service.service_package_handler.PremiumKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GeneratorResultServiceImpl implements GeneratorResultService {

    public static final String EMPTY_SING = "0";
    private final StaffListRecordService staffListRecordService;

    private final ServicePackageService servicePackageService;

    private final CalculateManager calculateManager;

    private final TimeSheetService timeSheetService;

    private final PremiumCategoryService premiumCategoryService;

    @Autowired
    public GeneratorResultServiceImpl(StaffListRecordService staffListRecordService, ServicePackageService servicePackageService,
                                      CalculateManager calculateManager, TimeSheetService timeSheetService,
                                      PremiumCategoryService premiumCategoryService) {
        this.staffListRecordService = staffListRecordService;
        this.servicePackageService = servicePackageService;
        this.calculateManager = calculateManager;
        this.timeSheetService = timeSheetService;
        this.premiumCategoryService = premiumCategoryService;
    }

    @Override
    public Result generate(StaffListRecord staffListRecord){

        log.info("Generate result. StaffListRecordId: {}", staffListRecord.getStaffListId());
        Float employmentPart = getEmploymentPart(staffListRecord);

        Float employmentUserPositionPart = getEmploymentUserPositionPart(staffListRecord);

        TimeSheet timeSheet = timeSheetService.findByStaffListRecordIdAndPeriod(staffListRecord.getStaffListId(),
                staffListRecord.getPeriod()).orElseThrow(()->new ResourceNotFoundException("timeSheet", "staffListRecordId", staffListRecord.getStaffListId()));

        Float hourCoefficient = timeSheet.getHourCoefficient();

        Result result = new Result(
                staffListRecord, staffListRecord.getEmployment(), employmentPart, employmentUserPositionPart, hourCoefficient, timeSheet.getNightHours()
        );

        if (Objects.requireNonNullElse(staffListRecord.getDepartment().getServicePackages(), EMPTY_SING).equals(EMPTY_SING)
                || Objects.requireNonNullElse(staffListRecord.getUserPosition().getServicePackageNumbers(), EMPTY_SING).equals(EMPTY_SING)) {
            return result;
        }

        List<ServicePackage> servicePackageListByUserPositionAndDepartment;

        try {
            servicePackageListByUserPositionAndDepartment = generateListUserPositionDepartment(staffListRecord);
        }catch (NullPointerException e){
            log.error("error {}, department: {}", e.getMessage(), staffListRecord.getDepartment().getName());
            return result;
        }
        if (servicePackageListByUserPositionAndDepartment.isEmpty()) {
            return result;
        }

        for (ServicePackage servicePackage: servicePackageListByUserPositionAndDepartment){
            calculateManager.calculate(servicePackage, result);
        }

        if (timeSheet.getUgrency() && staffListRecord.getPremiumCategory().getName().equals(PremiumKind.ZERO.name())){
            staffListRecord.setPremiumCategory(premiumCategoryService.findByName(PremiumKind.URG.name()).get());
        }

        calculateManager.calculate(staffListRecord, result);

        log.info("result: {}", result);

        return result;
    }

    private Float getEmploymentPart(StaffListRecord staffListRecord) {

        Float employmentSum = staffListRecordService.findByUserAndPeriod(staffListRecord.getUser(), staffListRecord.getPeriod())
                .stream()
                .map(StaffListRecord::getEmployment)
                .reduce(0f, Float::sum);

        return staffListRecord.getEmployment() / employmentSum;
    }

    private Float getEmploymentUserPositionPart(StaffListRecord staffListRecord){

        Float employmentUserPositionSum = staffListRecordService.findByUserAndUserPositionAndPeriod(staffListRecord.getUser(),
                        staffListRecord.getUserPosition(),
                        staffListRecord.getPeriod())
                .stream()
                .map(StaffListRecord::getEmployment)
                .reduce(0f, Float::sum);

        return staffListRecord.getEmployment() / employmentUserPositionSum;
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
}
