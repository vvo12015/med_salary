package net.vrakin.medsalary.service.service_package_handler;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.service.NSZU_DecryptionService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractCalculateStrategy {

    public static final String AMBULANCE_DEPARTMENT_PREFIX = "0175";
    public static final String AMBULANCE_ADDRESS = "ЗАКАРПАТСЬКА область, МУКАЧІВСЬКИЙ район, місто МУКАЧЕВО, вулиця Грушевського Михайла, 29";
    public static final String STATIONARY_ADDRESS = "ЗАКАРПАТСЬКА область, МУКАЧІВСЬКИЙ район, місто МУКАЧЕВО, вулиця Новака Андрія, 8-13";
    public static final String WOMAN_CONSULTATION = "019708";
    private final NSZU_DecryptionService nszu_decryptionService;

    public AbstractCalculateStrategy(NSZU_DecryptionService nszu_decryptionService) {
        this.nszu_decryptionService = nszu_decryptionService;
    }

    protected List<NszuDecryption> getNszuDecryptionList(ServicePackage servicePackage,
                                                         Result result){


        log.info("user: {}, servicePackageName: {}, userPosition: {}, placeProvider: {}",
                result.getUser().getName(), servicePackage.getFullName(), result.getUserPosition().getNszuName(), getPlaceProvide(result.getDepartment())
                        .substring(getPlaceProvide(result.getDepartment()).lastIndexOf(",")));

        log.info("ByServicePackage: {}, ByUserPosition: {}, ByPlaceProvider: {}",
                nszu_decryptionService.findByExecutorNameAndServicePackageName(result.getUser().getName() , servicePackage.getFullName()).size(),
                nszu_decryptionService.findByExecutorNameAndExecutorUserPosition(result.getUser().getName(), result.getUserPosition().getNszuName()).size(),
                nszu_decryptionService.findByExecutorNameAndProviderPlace(result.getUser().getName() , getPlaceProvide(result.getDepartment())).size());

        List<NszuDecryption> nszuDecryptionList = nszu_decryptionService
                .findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlace(
                        result.getUser().getName(),
                        result.getUserPosition().getNszuName(),
                        servicePackage.getFullName(),
                        getPlaceProvide(result.getDepartment()));

        log.info("nszuDecryptionListCount: {}", nszuDecryptionList.size());
        return nszuDecryptionList;
    }

    protected static boolean isValidSum(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected String getPlaceProvide(Department department){
        if (department.getDepartmentIsProId().startsWith(AMBULANCE_DEPARTMENT_PREFIX) ||
                department.getDepartmentIsProId().equals(WOMAN_CONSULTATION)){
            return AMBULANCE_ADDRESS;
        }else {
            return STATIONARY_ADDRESS;
        }
    }
}
