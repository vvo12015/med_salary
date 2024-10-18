package net.vrakin.medsalary.api;


import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.UserPositionDTO;
import net.vrakin.medsalary.exception.IdMismatchException;
import net.vrakin.medsalary.exception.ResourceExistException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.mapper.UserPositionMapper;
import net.vrakin.medsalary.service.UserPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-positions")
public class UserPositionRestController {

    private final UserPositionMapper userPositionMapper;
    private final UserPositionService userPositionService;

    @Autowired
    public UserPositionRestController(UserPositionMapper userPositionMapper, UserPositionService userPositionService) {
        this.userPositionMapper = userPositionMapper;
        this.userPositionService = userPositionService;
    }

    @GetMapping
    public ResponseEntity<List<UserPositionDTO>> getAll() {
        List<UserPositionDTO> userPositions = userPositionMapper.toDtoList(userPositionService.findAll());
        return new ResponseEntity<>(userPositions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPositionDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        UserPositionDTO userPosition = userPositionMapper.toDto(userPositionService.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("UserPosition", "id", id.toString())));

        return new ResponseEntity<>(userPosition, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserPositionDTO> add(@RequestBody UserPositionDTO userPositionDTO) {

        if (userPositionDTO.getId() != null) {
            throw new ResourceExistException("UserPositionId", userPositionDTO.getId().toString());
        }

        UserPosition userPosition = userPositionService.save(userPositionMapper.toEntity(userPositionDTO));

        UserPositionDTO savedUserPosition = userPositionMapper.toDto(userPosition);

        return new ResponseEntity<>(savedUserPosition, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            userPositionService.deleteById(id);
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("UserPosition", "id", id.toString());
        }
        return ResponseEntity.ok("UserPosition deleted successfully!.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserPositionDTO> updateDepartment(@PathVariable Long id, @RequestBody UserPositionDTO userPositionDTO) {

        if (!userPositionDTO.getId().equals(id)){
            throw new IdMismatchException("Department", id.toString(), userPositionDTO.getId().toString());
        }

        UserPosition userPosition = userPositionService.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("UserPosition", "id", id.toString()));

        userPosition.setName(userPositionDTO.getName());
        userPosition.setCodeIsPro(userPositionDTO.getCodeIsPro());
        userPosition.setMaxPoint(userPositionDTO.getMaxPoint());
        userPosition.setPointValue(userPositionDTO.getPointValue());
        userPosition.setBasicPremium(userPositionDTO.getBasicPremium());
        userPositionService.save(userPosition);

        UserPositionDTO savedUserPosition = userPositionMapper.toDto(userPosition);

        return new ResponseEntity<>(savedUserPosition, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<UserPositionDTO>> getByName(@PathVariable String name) throws ResourceNotFoundException {
        List<UserPositionDTO> userPositionDTOs = userPositionMapper.toDtoList(userPositionService.findByName(name));

        return new ResponseEntity<>(userPositionDTOs, HttpStatus.OK);
    }
}

