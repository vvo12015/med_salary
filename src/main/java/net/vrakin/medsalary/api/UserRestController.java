package net.vrakin.medsalary.api;

import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.dto.UserDTO;
import net.vrakin.medsalary.exception.IdMismatchException;
import net.vrakin.medsalary.exception.ResourceExistException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.mapper.UserMapper;
import net.vrakin.medsalary.service.DepartmentService;
import net.vrakin.medsalary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    private final DepartmentService departmentService;

    @Autowired
    public UserRestController(UserService userService,
                              UserMapper userMapper,
                              DepartmentService departmentService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = userMapper.toDtoList(userService.findAll());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        User user = userService.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User", "id", id.toString()));

        UserDTO userDTO = userMapper.toDto(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/login/{ipn}")
    public ResponseEntity<UserDTO> getByLogin(@PathVariable String IPN) throws ResourceNotFoundException {
        User user = userService.findByIPN(IPN)
                .orElseThrow(()->new ResourceNotFoundException("User", "IPN", IPN));

        UserDTO userDTO = userMapper.toDto(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/name/{namePattern}")
    public ResponseEntity<List<UserDTO>> getByLikeName(@PathVariable String namePattern) throws ResourceNotFoundException {
        List<UserDTO> users = userMapper.toDtoList(userService.findByNameLike(namePattern));

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> add(@RequestBody UserDTO userDto) {

        if (userDto.getId() != null) {
            throw new ResourceExistException("UserId", userDto.getId().toString());
        }

        User user = userMapper.toEntity(userDto);

        UserDTO savedUser = userMapper.toDto(userService.save(user));

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            userService.deleteById(id);
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("User", "id", id.toString());
        }
        return ResponseEntity.ok("User deleted successfully!.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {

        if (!userDTO.getId().equals(id)){
            throw new IdMismatchException("User", id.toString(), userDTO.getId().toString());
        }

        userService.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User", "id", id.toString()));


        User user = userMapper.toEntity(userDTO);


        UserDTO savedUser = userMapper.toDto(userService.save(user));

        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }
}

