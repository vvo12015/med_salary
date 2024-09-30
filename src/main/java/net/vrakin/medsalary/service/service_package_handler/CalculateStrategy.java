package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.UserPosition;

public interface CalculateStrategy {
    float calculate(ServicePackage servicePackage, UserPosition userPosition, String placeProvide, Float partEmployment);
//    boolean isValidPackage(ServicePackage servicePackage);
}
