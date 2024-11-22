package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.domain.UserPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffListRecordRepository extends JpaRepository<StaffListRecord, Long> {
    Optional<StaffListRecord> findByStaffListId(String staffListId);
    List<StaffListRecord> findByUserPosition(UserPosition userPosition);

    List<StaffListRecord> findByUser(User user);

    List<StaffListRecord> findByStartDate(LocalDateTime period);
}
