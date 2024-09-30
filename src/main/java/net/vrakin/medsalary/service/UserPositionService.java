package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.UserPosition;

import java.util.List;

public interface UserPositionService extends Service<UserPosition>{
    List<UserPosition> findByName(String name);
    List<UserPosition> findByCodeIsPro(String codeIsPro);
}
