package net.vrakin.medsalary.api;

import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.exception.IdMismatchException;
import net.vrakin.medsalary.exception.ResourceExistException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.generator.GeneratorStaffListRecordService;
import net.vrakin.medsalary.mapper.StaffListRecordMapper;
import net.vrakin.medsalary.service.StaffListRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контролер для управління записами списку персоналу.
 *
 * Забезпечує CRUD-операції для `StaffListRecord`.
 */
@RestController
@RequestMapping("/api/stafflist")
public class StaffListRecordRestController {

    private final StaffListRecordMapper staffListRecordMapper;
    private final StaffListRecordService staffListRecordService;
    private final GeneratorStaffListRecordService generatorStaffListRecordService;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param staffListRecordMapper маппер для перетворення між DTO і сутностями.
     * @param staffListRecordService сервіс для роботи із записами списку персоналу.
     * @param generatorStaffListRecordService сервіс для генерації об'єктів `StaffListRecord`.
     */
    @Autowired
    public StaffListRecordRestController(StaffListRecordMapper staffListRecordMapper,
                                         StaffListRecordService staffListRecordService,
                                         GeneratorStaffListRecordService generatorStaffListRecordService) {
        this.staffListRecordMapper = staffListRecordMapper;
        this.staffListRecordService = staffListRecordService;
        this.generatorStaffListRecordService = generatorStaffListRecordService;
    }

    /**
     * Отримати список усіх записів списку персоналу.
     *
     * @return список записів у форматі DTO.
     */
    @GetMapping
    public ResponseEntity<List<StaffListRecordDTO>> getAll() {
        List<StaffListRecordDTO> staffListRecordDTOList = staffListRecordMapper.toDtoList(staffListRecordService.findAll());
        return new ResponseEntity<>(staffListRecordDTOList, HttpStatus.OK);
    }

    /**
     * Отримати запис списку персоналу за ідентифікатором.
     *
     * @param id ідентифікатор запису.
     * @return DTO запису списку персоналу.
     * @throws ResourceNotFoundException якщо запис не знайдено.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StaffListRecordDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        StaffListRecordDTO staffListRecordDTO = staffListRecordMapper.toDto(staffListRecordService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StaffListRecord", "id", id.toString())));

        return new ResponseEntity<>(staffListRecordDTO, HttpStatus.OK);
    }

    /**
     * Додати новий запис до списку персоналу.
     *
     * @param staffListRecordDTO DTO нового запису.
     * @return створений запис у форматі DTO.
     * @throws ResourceExistException якщо ID вже існує.
     * @throws Exception якщо сталася помилка генерації запису.
     */
    @PostMapping
    public ResponseEntity<StaffListRecordDTO> add(@RequestBody StaffListRecordDTO staffListRecordDTO) throws Exception {

        if (staffListRecordDTO.getId() != null) {
            throw new ResourceExistException("StaffListRecordId", staffListRecordDTO.getId().toString());
        }

        StaffListRecord staffListRecord = staffListRecordService.save(
                generatorStaffListRecordService.generate(staffListRecordDTO));

        StaffListRecordDTO savedStaffListRecord = staffListRecordMapper.toDto(staffListRecord);

        return new ResponseEntity<>(savedStaffListRecord, HttpStatus.CREATED);
    }

    /**
     * Видалити запис списку персоналу за ідентифікатором.
     *
     * @param id ідентифікатор запису.
     * @return повідомлення про успішне видалення.
     * @throws ResourceNotFoundException якщо запис не знайдено.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            staffListRecordService.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("StaffListRecord", "id", id.toString());
        }
        return ResponseEntity.ok("StaffListRecord deleted successfully!.");
    }

    /**
     * Оновити існуючий запис списку персоналу.
     *
     * @param id ідентифікатор запису, що оновлюється.
     * @param staffListRecordDTO нові дані запису.
     * @return оновлений запис у форматі DTO.
     * @throws IdMismatchException якщо ID у DTO не збігається з переданим ID.
     * @throws ResourceNotFoundException якщо запис не знайдено.
     * @throws Exception якщо сталася помилка генерації запису.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StaffListRecordDTO> updateStaffListRecord(@PathVariable Long id, @RequestBody StaffListRecordDTO staffListRecordDTO) throws Exception {

        if (!staffListRecordDTO.getId().equals(id)) {
            throw new IdMismatchException("StaffListRecord", id.toString(), staffListRecordDTO.getId().toString());
        }

        staffListRecordService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StaffListRecord", "id", id.toString()));

        StaffListRecord staffListRecord = generatorStaffListRecordService.generate(staffListRecordDTO);

        StaffListRecordDTO savedStaffListRecordDTO = staffListRecordMapper.toDto(staffListRecordService.save(staffListRecord));

        return new ResponseEntity<>(savedStaffListRecordDTO, HttpStatus.OK);
    }
}
