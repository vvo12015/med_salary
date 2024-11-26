package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ResultService extends Service<Result>{
    List<Result> findByUserAndPeriod(User user, LocalDate period);
}
