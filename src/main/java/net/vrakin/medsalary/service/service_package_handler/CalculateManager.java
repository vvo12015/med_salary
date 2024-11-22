package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.StaffListRecord;

import java.util.HashMap;
import java.util.Map;

public interface CalculateManager {
    void calculate(ServicePackage servicePackage, Result result);
    void calculate(StaffListRecord staffListRecord, Result result);
}
