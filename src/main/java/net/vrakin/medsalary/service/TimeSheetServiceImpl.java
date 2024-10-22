package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.TimeSheet;
import net.vrakin.medsalary.repository.TimeSheetRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TimeSheetServiceImpl extends AbstractService<TimeSheet> implements TimeSheetService{

    private final TimeSheetRepository timeSheetRepository;

    protected TimeSheetServiceImpl(TimeSheetRepository repository) {
        super(repository);
        timeSheetRepository = repository;
    }

    @Override
    public Optional<TimeSheet> findByStaffListRecordId(String staffListId) {
        return timeSheetRepository.findByStaffListRecordId(staffListId);
    }

    @Override
    public Float sumVlkTime() {
        return timeSheetRepository.sumVlkTime();
    }
}
