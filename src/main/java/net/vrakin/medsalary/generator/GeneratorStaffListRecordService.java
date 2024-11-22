package net.vrakin.medsalary.generator;

import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.StaffListRecordDTO;

import java.time.LocalDate;

public interface GeneratorStaffListRecordService{
    StaffListRecord generate(StaffListRecordDTO source) throws Exception;
}
