package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentServiceImpl extends AbstractService<Department> implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        super(departmentRepository);
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Optional<Department> findByName(String name){
        return departmentRepository.findByName(name);
    }

    @Override
    public Optional<Department> findByStaffListRecord(String staffListRecords) {
        return departmentRepository.findByStaffListRecords(staffListRecords);
    }

    @Override
    public Optional<Department> findByDepartmentIsProId(String departmentIsProId) {
        return departmentRepository.findByDepartmentIsProId(departmentIsProId);
    }
}
