package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.repository.UserPositionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserPositionServiceImpl extends AbstractService<UserPosition> implements UserPositionService {

    private final UserPositionRepository userPositionRepository;

    public UserPositionServiceImpl(UserPositionRepository userPositionRepository) {
        super(userPositionRepository);
        this.userPositionRepository = userPositionRepository;
    }

    @Override
    public List<UserPosition> findByName(String name) {
        return userPositionRepository.findByName(name);
    }

    @Override
    public List<UserPosition> findByCodeIsProAndPeriod(String codeIsPro, LocalDate period) {
        return userPositionRepository.findByCodeIsProAndPeriod(codeIsPro, period);
    }

    @Override
    public List<UserPosition> findByPeriod(LocalDate period) {
        return userPositionRepository.findByPeriod(period);
    }

    @Override
    public List<UserPosition> findByCodeIsPro(String codeIsPro) {
        return userPositionRepository.findByCodeIsPro(codeIsPro);
    }
}
