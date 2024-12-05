package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.repository.StaffListRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StaffListRecordServiceImpl extends AbstractService<StaffListRecord> implements StaffListRecordService {

    private final StaffListRecordRepository staffListRecordRepository;

    @Autowired
    public StaffListRecordServiceImpl(StaffListRecordRepository staffListRecordRepository) {
        super(staffListRecordRepository);
        this.staffListRecordRepository = staffListRecordRepository;
    }

    @Override
    public Optional<StaffListRecord> findByStaffListId(String staffListId) {
        return staffListRecordRepository.findByStaffListId(staffListId);
    }

    @Override
    public List<StaffListRecord> findByUserPosition(UserPosition userPosition) {
        return staffListRecordRepository.findByUserPosition(userPosition);
    }

    @Override
    public List<StaffListRecord> findByUserAndPeriod(User user, LocalDate period){
        return staffListRecordRepository.findByUserAndStartDate(user, period.atTime(0, 0));
    }

    @Override
    public List<StaffListRecord> findByPeriod(LocalDate period) {
        LocalDateTime periodWithTime = period.plusMonths(1).atTime(0, 0);
        return staffListRecordRepository.findByStartDateAndEmploymentStartDateLessThan(periodWithTime, period);
    }

    @Override
    public List<StaffListRecord> findByUser(User user) {
        return staffListRecordRepository.findByUser(user);
    }

    @Override
    public List<StaffListRecord> findByUserAndUserPositionAndPeriod(User user, UserPosition userPosition, LocalDate period){
        return staffListRecordRepository.findByUserAndUserPositionAndStartDate(user, userPosition, period.atTime(0, 0));
    }
}
