package net.vrakin.medsalary.service.service_package_handler;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CalculateManagerImpl implements CalculateManager{

    private final Map<String, CalculateStrategyNSZU> calculateStrategyNZSU_Map = new HashMap<>();
    private final Map<String, CalculateStrategy> calculateStrategyMap = new HashMap<>();


    static final Map<String, String> premiumMap = new HashMap<>();

    static {
        premiumMap.put("DIAG", "Діагностика");
        premiumMap.put("DIAL", "Діаліз");
        premiumMap.put("UZD", "УЗД амбулаторно");
        premiumMap.put("URG", "Невідкладна допомога");
    }

    @Autowired
    public CalculateManagerImpl(NSZU_DecryptionService nszu_decryptionService) {

        calculateStrategyNZSU_Map.put("4", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("5", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("6", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("7", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("8", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("17", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("23", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("38", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("53", new CalculateByStationaryNoOperation(nszu_decryptionService));

        calculateStrategyNZSU_Map.put("9", new CalculateByAmbulatoryNoOperation(nszu_decryptionService));

        calculateStrategyNZSU_Map.put("47", new CalculateByOneDaySurgery(nszu_decryptionService));

        calculateStrategyNZSU_Map.put("10", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("11", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("12", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("13", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("14", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("15", new CalculateByPriorityServicePackage(nszu_decryptionService));

        calculateStrategyMap.put("premium_category", new CalculateByPremiumCategory());
    }

    @Override
    public void calculate(ServicePackage servicePackage, Result result) {

        CalculateStrategyNSZU calculatorNSZU = calculateStrategyNZSU_Map.get(servicePackage.getNumber());

        if (Objects.nonNull(calculatorNSZU)) calculatorNSZU.calculate(servicePackage, result);
    }

    @Override
    public void calculate(StaffListRecord staffListRecord, Result result){
        for ( Map.Entry<String, CalculateStrategy> calculator :
                calculateStrategyMap.entrySet()) {
            calculator.getValue().calculate(staffListRecord, result);
        }
    }
}
