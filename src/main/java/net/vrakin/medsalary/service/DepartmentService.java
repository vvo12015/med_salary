package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.Department;

import java.util.Optional;

public interface DepartmentService extends Service<Department>{
    Optional<Department> findByName(String name);
    Optional<Department> findByStaffListRecord(String staffListRecords);

    Optional<Department> findByDepartmentIsProId(String departmentIsProId);
}
