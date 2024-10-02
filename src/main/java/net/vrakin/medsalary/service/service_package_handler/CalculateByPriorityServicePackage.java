package net.vrakin.medsalary.service.service_package_handler;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculateByPriorityServicePackage extends AbstractCalculateStrategy implements CalculateStrategy {

    public static final float PACKAGE_COST = 80f/3;
    @Autowired
    public CalculateByPriorityServicePackage(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    @Override
    public float calculate(ServicePackage servicePackage, UserPosition userPosition, String placeProvide
            , Float partEmployment) {
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, userPosition, placeProvide);

        return PACKAGE_COST * nszuDecryptionList.size() * partEmployment;
    }

}
