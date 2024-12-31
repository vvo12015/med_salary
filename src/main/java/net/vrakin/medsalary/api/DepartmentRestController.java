package net.vrakin.medsalary.api;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.dto.DepartmentDTO;
import net.vrakin.medsalary.exception.IdMismatchException;
import net.vrakin.medsalary.exception.ResourceExistException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.mapper.DepartmentMapper;
import net.vrakin.medsalary.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контролер для управління операціями з відділами.
 *
 * Забезпечує CRUD-операції та пошук відділів.
 */
@RestController
@RequestMapping("/api/departments")
@Slf4j
public class DepartmentRestController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param departmentService сервіс для роботи з відділами.
     * @param departmentMapper маппер для перетворення між DTO і сутностями.
     */
    @Autowired
    public DepartmentRestController(DepartmentService departmentService,
                                    DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Отримати всі відділи.
     *
     * @return список усіх відділів у форматі DTO.
     */
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAll() {
        List<DepartmentDTO> departments = departmentMapper.toDtoList(departmentService.findAll());
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    /**
     * Отримати відділ за його ідентифікатором.
     *
     * @param id ідентифікатор відділу.
     * @return DTO відділу.
     * @throws ResourceNotFoundException якщо відділ не знайдено.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        DepartmentDTO department = departmentMapper.toDto(departmentService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id.toString())));

        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    /**
     * Додати новий відділ.
     *
     * @param departmentDTO дані нового відділу.
     * @return створений відділ у форматі DTO.
     * @throws ResourceExistException якщо ID вже існує.
     */
    @PostMapping
    public ResponseEntity<DepartmentDTO> add(@RequestBody DepartmentDTO departmentDTO) {

        if (departmentDTO.getId() != null) {
            throw new ResourceExistException("DepartmentId", departmentDTO.getId().toString());
        }

        Department department = departmentService.save(departmentMapper.toEntity(departmentDTO));
        DepartmentDTO savedDepartment = departmentMapper.toDto(department);

        return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
    }

    /**
     * Видалити відділ за ідентифікатором.
     *
     * @param id ідентифікатор відділу.
     * @return повідомлення про успішне видалення.
     * @throws ResourceNotFoundException якщо відділ не знайдено.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            departmentService.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Department", "id", id.toString());
        }
        return ResponseEntity.ok("Department deleted successfully!.");
    }

    /**
     * Оновити інформацію про відділ.
     *
     * @param id ідентифікатор відділу, що оновлюється.
     * @param departmentDTO нові дані відділу.
     * @return оновлений відділ у форматі DTO.
     * @throws IdMismatchException якщо ID у запиті та у DTO не збігаються.
     * @throws ResourceNotFoundException якщо відділ не знайдено.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {

        log.info("Editing department with id: {} and name: {}", id, departmentDTO.getName());

        if (!departmentDTO.getId().equals(id)) {
            throw new IdMismatchException("Department", id.toString(), departmentDTO.getId().toString());
        }

        Department department = departmentService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id.toString()));

        department.setDepartmentIsProId(departmentDTO.getDepartmentIsProId());
        department.setDepartmentTemplateId(departmentDTO.getDepartmentTemplateId());
        department.setName(departmentDTO.getName());
        department.setNameEleks(departmentDTO.getNameEleks());

        departmentService.save(department);

        DepartmentDTO savedDepartmentDTO = departmentMapper.toDto(departmentService.save(department));

        return new ResponseEntity<>(savedDepartmentDTO, HttpStatus.OK);
    }

    /**
     * Отримати відділ за назвою.
     *
     * @param name назва відділу.
     * @return DTO відділу.
     * @throws ResourceNotFoundException якщо відділ не знайдено.
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<DepartmentDTO> getByName(@PathVariable String name) throws ResourceNotFoundException {
        Department department = departmentService.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "name", name));

        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        return new ResponseEntity<>(departmentDTO, HttpStatus.OK);
    }
}
