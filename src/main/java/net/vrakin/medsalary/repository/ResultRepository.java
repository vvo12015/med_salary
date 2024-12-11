package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByUserAndDate(User user, LocalDate date);

    List<Result> findByStaffListRecordAndDate(StaffListRecord staffListRecord, LocalDate period);
}
