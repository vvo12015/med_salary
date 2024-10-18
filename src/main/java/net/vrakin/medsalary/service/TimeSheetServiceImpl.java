package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.TimeSheet;
import net.vrakin.medsalary.repository.TimeSheetRepository;
import org.springframework.stereotype.Service;

@Service
public class TimeSheetServiceImpl extends AbstractService<TimeSheet> implements TimeSheetService{

    protected TimeSheetServiceImpl(TimeSheetRepository repository) {
        super(repository);
    }
}
