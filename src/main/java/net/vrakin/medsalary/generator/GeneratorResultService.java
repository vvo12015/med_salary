package net.vrakin.medsalary.generator;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.exception.CalculateTypeNotFoundException;

import java.time.LocalDate;

public interface GeneratorResultService{
    Result generate(StaffListRecord source) throws Exception;
}
