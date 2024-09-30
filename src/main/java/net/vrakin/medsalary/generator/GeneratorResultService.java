package net.vrakin.medsalary.generator;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.exception.CalculateTypeNotFoundException;

public interface GeneratorResultService {
    Result generateResult(StaffListRecord staffListRecord) throws CalculateTypeNotFoundException;
}
