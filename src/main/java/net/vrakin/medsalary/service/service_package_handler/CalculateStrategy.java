package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;

public interface CalculateStrategy {
    void calculate(StaffListRecord staffListRecord, Result result);
}
