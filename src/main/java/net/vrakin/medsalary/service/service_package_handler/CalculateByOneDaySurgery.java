package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculateByOneDaySurgery extends AbstractCalculateStrategyNSZU implements CalculateStrategyNSZU {

    public static final float PACKAGE_COST = 250f;

    @Autowired
    public CalculateByOneDaySurgery(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, result);

        result.setCountEMR_oneDaySurgery(nszuDecryptionList.size());

        result.setOneDaySurgeryPremium(result.getOneDaySurgeryPremium() +
                + PACKAGE_COST * nszuDecryptionList.size());
    }
}
