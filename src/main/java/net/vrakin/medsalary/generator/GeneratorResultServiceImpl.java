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

    private final CalculateManager calculateManager;

    @Autowired
    public GeneratorResultServiceImpl(StaffListRecordService staffListRecordService, ServicePackageService servicePackageService,
                                      CalculateManager calculateManager) {
        this.staffListRecordService = staffListRecordService;
        this.servicePackageService = servicePackageService;
        this.calculateManager = calculateManager;
    }

    @Override
    public Result generateResult(StaffListRecord staffListRecord) throws ResourceNotFoundException {

        Float employmentPart = getEmploymentPart(staffListRecord, staffListRecord.getUser());
        Result result = new Result(staffListRecord.getUser(), staffListRecord.getUserPosition(), staffListRecord.getDepartment(), employmentPart);

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

        log.info("result: {}", result);
        return result;
    }

    private Float getEmploymentPart(StaffListRecord staffListRecord, User user) {
        Float employmentSum =
                staffListRecordService.findByUser(user)
                        .stream()
                        .map(StaffListRecord::getEmployment)
                        .reduce(0f, Float::sum);
        return staffListRecord.getEmployment() / employmentSum;
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
