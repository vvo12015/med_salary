package net.vrakin.medsalary.generator;

import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.StaffListRecordDTO;

public interface GeneratorStaffListRecordService {
    StaffListRecord generate(StaffListRecordDTO staffListRecordDTO);
}
