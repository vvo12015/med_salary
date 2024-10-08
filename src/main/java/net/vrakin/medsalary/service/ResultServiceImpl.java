package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.repository.ResultRepository;
import org.springframework.stereotype.Service;

@Service
public class ResultServiceImpl extends AbstractService<Result> implements ResultService {
    private ResultRepository resultRepository;

    public ResultServiceImpl(ResultRepository resultRepository) {
        super(resultRepository);
        this.resultRepository = resultRepository;
    }
}
