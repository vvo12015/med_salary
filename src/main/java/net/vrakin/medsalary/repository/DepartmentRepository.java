package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
    @Query("SELECT dep FROM Department dep " +
            "JOIN dep.staffListRecords slr " +
            "WHERE slr.staffListId = :staffListId")
    Optional<Department> findByStaffListRecords(@Param("staffListId")String staffListRecordList);

    Optional<Department> findByDepartmentIsProId(String departmentIsProId);

    Optional<Department> findByDepartmentIsProIdAndPeriod(String departmentIsProId, LocalDate period);

    List<Department> findByPeriod(LocalDate period);
}
