package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.TimeSheet;

import java.time.LocalDate;
import java.util.Optional;

public interface TimeSheetService extends Service<TimeSheet>{
    Optional<TimeSheet> findByStaffListRecordId(String staffListId);
    Optional<TimeSheet> findByStaffListRecordIdAndPeriod(String staffListId, LocalDate period);
}
