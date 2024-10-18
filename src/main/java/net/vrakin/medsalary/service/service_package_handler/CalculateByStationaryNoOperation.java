package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CalculateByStationaryNoOperation extends AbstractCalculateStrategy implements CalculateStrategy {

    private final float PACKAGE_COST = 200F / 3;

        @Autowired
    public CalculateByStationaryNoOperation(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, result);

        result.setCountEMR_stationary(Objects.requireNonNullElse(result.getCountEMR_stationary(), 0) + nszuDecryptionList.size());

        if (!nszuDecryptionList.isEmpty()){
            result.setHospNSZU_Premium(
                    result.getHospNSZU_Premium() + nszuDecryptionList.size() * PACKAGE_COST * result.getEmploymentPart()
            );
        }

    }

}
