package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CalculateManagerImpl implements CalculateManager {

    private List<String> stationaryServicePackageNumbers = new ArrayList<>();
    private List<String> ambularotyServicePackageNumbers = new ArrayList<>();
    private List<String> priorityServicePackageNumbers = new ArrayList<>();

    private final NSZU_DecryptionService nszu_decryptionService;

    @Autowired
    public CalculateManagerImpl(NSZU_DecryptionService nszu_decryptionService) {
        this.nszu_decryptionService = nszu_decryptionService;

        stationaryServicePackageNumbers.add("4");
        stationaryServicePackageNumbers.add("17");
        stationaryServicePackageNumbers.add("23");
        stationaryServicePackageNumbers.add("38");

        ambularotyServicePackageNumbers.add("9");
        ambularotyServicePackageNumbers.add("35");

        priorityServicePackageNumbers.add("10");
        priorityServicePackageNumbers.add("11");
        priorityServicePackageNumbers.add("12");
        priorityServicePackageNumbers.add("13");
        priorityServicePackageNumbers.add("14");
        priorityServicePackageNumbers.add("15");
    }

    public void calculate(ServicePackage servicePackage, Result result) {

        CalculateStrategy calculator = null;

        if (stationaryServicePackageNumbers.contains(servicePackage.getNumber())){
            calculator = new CalculateByStationaryNoOperation(nszu_decryptionService);
        }

        if (ambularotyServicePackageNumbers.contains(servicePackage.getNumber())){
            calculator = new CalculateByAmbulatoryNoOperation(nszu_decryptionService);
        }

        if (servicePackage.getNumber().equals(ONE_SERVICE_PACKAGE_ONE_DAY_SERGERY_NUMBER)){
            calculator = new CalculateByOneDaySurgery(nszu_decryptionService);
        }

        if (priorityServicePackageNumbers.contains(servicePackage.getNumber())){
            calculator = new CalculateByPriorityServicePackage(nszu_decryptionService);
        }

        if (Objects.nonNull(calculator)) calculator.calculate(servicePackage, result);
    }


}
