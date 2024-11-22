package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.UserPosition;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface UserPositionService extends Service<UserPosition>{
    List<UserPosition> findByName(String name);
    List<UserPosition> findByCodeIsPro(String codeIsPro);

    List<UserPosition> findByCodeIsProAndPeriod(String codeIsPro, LocalDate period);

    List<UserPosition> findByPeriod(LocalDate period);
}
