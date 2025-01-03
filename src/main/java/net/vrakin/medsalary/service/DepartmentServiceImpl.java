package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Реалізація сервісу {@link DepartmentService} для управління підрозділами.
 *
 * <p>Використовує репозиторій {@link DepartmentRepository} для взаємодії з базою даних.</p>
 *
 * <p>Основні можливості:
 * <ul>
 *     <li>Пошук підрозділу за назвою.</li>
 *     <li>Пошук підрозділу за унікальним кодом DepartmentIsProId.</li>
 *     <li>Пошук підрозділу за унікальним кодом DepartmentIsProId та періодом.</li>
 *     <li>Пошук списку підрозділів за періодом.</li>
 *     <li>Пошук підрозділу за записами у штатному розписі.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
@Service
public class DepartmentServiceImpl extends AbstractService<Department> implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    /**
     * Конструктор для ін'єкції залежностей.
     *
     * @param departmentRepository Репозиторій підрозділів {@link DepartmentRepository}.
     */
    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        super(departmentRepository);
        this.departmentRepository = departmentRepository;
    }

    /**
     * Знаходить підрозділ за назвою.
     *
     * @param name Назва підрозділу.
     * @return Об'єкт {@link Optional}, що містить підрозділ, якщо знайдено.
     */
    @Override
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }

    /**
     * Знаходить підрозділ за унікальним кодом DepartmentIsProId та періодом.
     *
     * @param departmentIsProId Унікальний код підрозділу.
     * @param period Період для пошуку підрозділу.
     * @return Об'єкт {@link Optional}, що містить підрозділ, якщо знайдено.
     */
    @Override
    public Optional<Department> findByDepartmentIsProIdAndPeriod(String departmentIsProId, LocalDate period) {
        return departmentRepository.findByDepartmentIsProIdAndPeriod(departmentIsProId, period);
    }

    /**
     * Знаходить список підрозділів за вказаним періодом.
     *
     * @param period Період для пошуку підрозділів.
     * @return Список підрозділів, що відповідають вказаному періоду.
     */
    @Override
    public List<Department> findByPeriod(LocalDate period) {
        return departmentRepository.findByPeriod(period);
    }

    /**
     * Знаходить підрозділ за записами у штатному розписі.
     *
     * @param staffListRecords Запис(и) у штатному розписі.
     * @return Об'єкт {@link Optional}, що містить підрозділ, якщо знайдено.
     */
    @Override
    public Optional<Department> findByStaffListRecord(String staffListRecords) {
        return departmentRepository.findByStaffListRecords(staffListRecords);
    }

    /**
     * Знаходить підрозділ за унікальним кодом DepartmentIsProId.
     *
     * @param departmentIsProId Унікальний код підрозділу.
     * @return Об'єкт {@link Optional}, що містить підрозділ, якщо знайдено.
     */
    @Override
    public Optional<Department> findByDepartmentIsProId(String departmentIsProId) {
        return departmentRepository.findByDepartmentIsProId(departmentIsProId);
    }
}