package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalculateManagerImpl implements CalculateManager {

    private final Map<String, CalculateStrategy> calculateStrategyMap = new HashMap<>();

    @Autowired
    public CalculateManagerImpl(NSZU_DecryptionService nszu_decryptionService) {

        calculateStrategyMap.put("4", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyMap.put("17", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyMap.put("23", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyMap.put("38", new CalculateByStationaryNoOperation(nszu_decryptionService));


        calculateStrategyMap.put("9", new CalculateByAmbulatoryNoOperation(nszu_decryptionService));
        //не рахуємо
        //calculateStrategyMap.put("35", new CalculateByAmbulatoryNoOperation(nszu_decryptionService));

        calculateStrategyMap.put("47", new CalculateByOneDaySurgery(nszu_decryptionService));

        calculateStrategyMap.put("10", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyMap.put("11", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyMap.put("12", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyMap.put("13", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyMap.put("14", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyMap.put("15", new CalculateByPriorityServicePackage(nszu_decryptionService));

        calculateStrategyMap.put("60", new CalculateVlk(nszu_decryptionService));
    }

    public void calculate(ServicePackage servicePackage, Result result) {

        CalculateStrategy calculator = calculateStrategyMap.get(servicePackage.getNumber());

        if (Objects.nonNull(calculator)) calculator.calculate(servicePackage, result);
    }

}
