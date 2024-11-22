package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.domain.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {
    Optional<TimeSheet> findByStaffListRecordId(String staffListRecordId);

    Optional<TimeSheet> findByStaffListRecordIdAndPeriod(String staffListId, LocalDate period);
}
