package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.Department;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DepartmentService extends Service<Department>{
    Optional<Department> findByName(String name);


    List<Department> findByPeriod(LocalDate period);

    Optional<Department> findByStaffListRecord(String staffListRecords);

    Optional<Department> findByDepartmentIsProId(String departmentIsProId);

    Optional<Department> findByDepartmentIsProIdAndPeriod(String departmentIsProId, LocalDate period);

}
