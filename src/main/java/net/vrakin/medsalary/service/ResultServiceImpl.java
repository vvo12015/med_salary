package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.repository.ResultRepository;
import net.vrakin.medsalary.repository.StaffListRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ResultServiceImpl extends AbstractService<Result> implements ResultService {

    private final ResultRepository resultRepository;

    @Autowired
    public ResultServiceImpl(ResultRepository resultRepository) {
        super(resultRepository);
        this.resultRepository = resultRepository;
    }

    @Override
    public List<Result> findByUserAndPeriod(User user, LocalDate period) {
        return resultRepository.findByUserAndDate(user, period);
    }
}
