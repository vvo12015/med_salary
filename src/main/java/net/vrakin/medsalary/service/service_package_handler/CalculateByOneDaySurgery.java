package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculateByOneDaySurgery extends AbstractCalculateStrategy implements CalculateStrategy {

    public static final float PACKAGE_COST = 160f;

    @Autowired
    public CalculateByOneDaySurgery(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, result);


        result.setOneDaySurgery(result.getOneDaySurgery() +
                + PACKAGE_COST * nszuDecryptionList.size() * result.getEmploymentPart());
    }
}
