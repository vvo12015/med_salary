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

@RestController
@RequestMapping("/api/stafflist")
public class StaffListRecordRestController {

    private final StaffListRecordMapper staffListRecordMapper;

    private final StaffListRecordService staffListRecordService;

    private final GeneratorStaffListRecordService generatorStaffListRecordService;

    @Autowired
    public StaffListRecordRestController(StaffListRecordMapper staffListRecordMapper, StaffListRecordService staffListRecordService,
                                         GeneratorStaffListRecordService generatorStaffListRecordService) {
        this.staffListRecordMapper = staffListRecordMapper;
        this.staffListRecordService = staffListRecordService;
        this.generatorStaffListRecordService = generatorStaffListRecordService;
    }

    @GetMapping
    public ResponseEntity<List<StaffListRecordDTO>> getAll() {
        List<StaffListRecordDTO> staffListRecordDTOList = staffListRecordMapper.toDtoList(staffListRecordService.findAll());
        return new ResponseEntity<>(staffListRecordDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffListRecordDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        StaffListRecordDTO staffListRecordDTO = staffListRecordMapper.toDto(staffListRecordService.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("StaffListRecord", "id", id.toString())));

        return new ResponseEntity<>(staffListRecordDTO, HttpStatus.OK);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            staffListRecordService.deleteById(id);
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("StaffListRecord", "id", id.toString());
        }
        return ResponseEntity.ok("StaffListRecord deleted successfully!.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffListRecordDTO> updateStaffListRecord(@PathVariable Long id, @RequestBody StaffListRecordDTO staffListRecordDTO) throws Exception {

        if (!staffListRecordDTO.getId().equals(id)){
            throw new IdMismatchException("StaffListRecord", id.toString(), staffListRecordDTO.getId().toString());
        }

        staffListRecordService.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("StaffListRecord", "id", id.toString()));


        StaffListRecord staffListRecord = generatorStaffListRecordService.generate(staffListRecordDTO);


        StaffListRecordDTO savedStaffListRecordDTO = staffListRecordMapper.toDto(staffListRecordService.save(staffListRecord));

        return new ResponseEntity<>(savedStaffListRecordDTO, HttpStatus.OK);
    }
}

