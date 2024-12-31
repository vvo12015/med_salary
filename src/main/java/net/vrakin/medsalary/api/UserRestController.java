package net.vrakin.medsalary.api;

import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.dto.UserDTO;
import net.vrakin.medsalary.exception.IdMismatchException;
import net.vrakin.medsalary.exception.ResourceExistException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.mapper.UserMapper;
import net.vrakin.medsalary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контролер для управління користувачами.
 *
 * Забезпечує CRUD-операції та пошук користувачів за різними параметрами.
 */
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param userService сервіс для роботи з користувачами.
     * @param userMapper маппер для перетворення між DTO і сутностями.
     */
    @Autowired
    public UserRestController(UserService userService,
                              UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Отримати список усіх користувачів.
     *
     * @return список користувачів у форматі DTO.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = userMapper.toDtoList(userService.findAll());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Отримати користувача за ідентифікатором.
     *
     * @param id ідентифікатор користувача.
     * @return DTO користувача.
     * @throws ResourceNotFoundException якщо користувача не знайдено.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));

        UserDTO userDTO = userMapper.toDto(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * Отримати користувача за ІПН (ідентифікаційним податковим номером).
     *
     * @param ipn ІПН користувача.
     * @return DTO користувача.
     * @throws ResourceNotFoundException якщо користувача не знайдено.
     */
    @GetMapping("/login/{ipn}")
    public ResponseEntity<UserDTO> getByLogin(@PathVariable String ipn) throws ResourceNotFoundException {
        User user = userService.findByIPN(ipn)
                .orElseThrow(() -> new ResourceNotFoundException("User", "IPN", ipn));

        UserDTO userDTO = userMapper.toDto(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * Отримати користувачів за частковим збігом імені.
     *
     * @param namePattern шаблон імені.
     * @return список користувачів у форматі DTO.
     */
    @GetMapping("/name/{namePattern}")
    public ResponseEntity<List<UserDTO>> getByLikeName(@PathVariable String namePattern) throws ResourceNotFoundException {
        List<UserDTO> users = userMapper.toDtoList(userService.findByNameLike(namePattern));

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Додати нового користувача.
     *
     * @param userDto DTO нового користувача.
     * @return створений користувач у форматі DTO.
     * @throws ResourceExistException якщо ID вже існує.
     */
    @PostMapping
    public ResponseEntity<UserDTO> add(@RequestBody UserDTO userDto) {

        if (userDto.getId() != null) {
            throw new ResourceExistException("UserId", userDto.getId().toString());
        }

        User user = userMapper.toEntity(userDto);

        UserDTO savedUser = userMapper.toDto(userService.save(user));

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Видалити користувача за ідентифікатором.
     *
     * @param id ідентифікатор користувача.
     * @return повідомлення про успішне видалення.
     * @throws ResourceNotFoundException якщо користувача не знайдено.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            userService.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User", "id", id.toString());
        }
        return ResponseEntity.ok("User deleted successfully!.");
    }

    /**
     * Оновити існуючого користувача.
     *
     * @param id ідентифікатор користувача.
     * @param userDTO нові дані користувача.
     * @return оновлений користувач у форматі DTO.
     * @throws IdMismatchException якщо ID у DTO не збігається з переданим ID.
     * @throws ResourceNotFoundException якщо користувача не знайдено.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {

        if (!userDTO.getId().equals(id)) {
            throw new IdMismatchException("User", id.toString(), userDTO.getId().toString());
        }

        userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));

        User user = userMapper.toEntity(userDTO);

        UserDTO savedUser = userMapper.toDto(userService.save(user));

        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }
}
