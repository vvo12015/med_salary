package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.domain.UserPosition;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StaffListRecordService extends Service<StaffListRecord>{
    Optional<StaffListRecord> findByStaffListId(String staffListId);
    List<StaffListRecord> findByUserPosition(UserPosition userPosition);
    List<StaffListRecord> findByUser(User user);

    List<StaffListRecord> findByPeriod(LocalDate period);
}
