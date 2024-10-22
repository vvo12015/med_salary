package net.vrakin.medsalary.service.service_package_handler;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.service.NSZU_DecryptionService;

import java.util.List;

@Slf4j
public abstract class AbstractCalculateStrategy {

    public static final String AMBULANCE_DEPARTMENT_PREFIX = "0175";
    public static final String AMBULANCE_ADDRESS = "ЗАКАРПАТСЬКА область, МУКАЧІВСЬКИЙ район, місто МУКАЧЕВО, вулиця Грушевського Михайла, 29";
    public static final String STATIONARY_ADDRESS = "ЗАКАРПАТСЬКА область, МУКАЧІВСЬКИЙ район, місто МУКАЧЕВО, вулиця Новака Андрія, 8-13";
    public static final String WOMAN_CONSULTATION = "019708";
    protected final NSZU_DecryptionService nszu_decryptionService;

    public AbstractCalculateStrategy(NSZU_DecryptionService nszu_decryptionService) {
        this.nszu_decryptionService = nszu_decryptionService;
    }

    protected List<NszuDecryption> getNszuDecryptionList(ServicePackage servicePackage,
                                                         Result result){

        List<NszuDecryption> nszuDecryptionList = nszu_decryptionService
                .findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlace(
                        result.getUser().getName(),
                        result.getUserPosition().getNszuName(),
                        servicePackage.getFullName(),
                        getPlaceProvide(result.getDepartment()));


        List<NszuDecryption> nszuResult =  nszuDecryptionList.stream().filter(n->(n.isReportStatus() && n.isStatisticStatus())).toList();

        return nszuResult;
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
