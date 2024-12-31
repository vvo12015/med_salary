package net.vrakin.medsalary.api;

import net.vrakin.medsalary.domain.SecurityRole;
import net.vrakin.medsalary.domain.SecurityUser;
import net.vrakin.medsalary.dto.SecurityUserDTO;
import net.vrakin.medsalary.exception.IdMismatchException;
import net.vrakin.medsalary.exception.ResourceExistException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.mapper.SecurityUserMapper;
import net.vrakin.medsalary.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * REST-контролер для управління користувачами безпеки.
 *
 * Забезпечує CRUD-операції та функціонал для роботи з ролями користувачів.
 */
@RestController
@RequestMapping("/api/s-users")
public class SecurityUserRestController {

    public static final String ENTITY_NAME = "SecurityUser";
    private final SecurityUserService securityUserService;
    private final SecurityUserMapper securityUserMapper;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param securityUserService сервіс для роботи з користувачами.
     * @param securityUserMapper маппер для перетворення між DTO і сутностями.
     */
    @Autowired
    public SecurityUserRestController(SecurityUserService securityUserService,
                                      SecurityUserMapper securityUserMapper) {
        this.securityUserService = securityUserService;
        this.securityUserMapper = securityUserMapper;
    }

    /**
     * Отримати список усіх користувачів.
     *
     * @return список користувачів у форматі DTO.
     */
    @GetMapping
    public ResponseEntity<List<SecurityUserDTO>> getAll() {
        List<SecurityUserDTO> users = securityUserMapper.toDtoList(securityUserService.findAll());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Отримати користувача за його ідентифікатором.
     *
     * @param id ідентифікатор користувача.
     * @return DTO користувача.
     * @throws ResourceNotFoundException якщо користувача не знайдено.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SecurityUserDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        SecurityUser user = securityUserService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, "id", id.toString()));

        SecurityUserDTO userDTO = securityUserMapper.toDto(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * Отримати користувача за логіном.
     *
     * @param login логін користувача.
     * @return DTO користувача.
     * @throws ResourceNotFoundException якщо користувача не знайдено.
     */
    @GetMapping("/login/{login}")
    public ResponseEntity<SecurityUserDTO> getByLogin(@PathVariable String login) throws ResourceNotFoundException {
        SecurityUser user = securityUserService.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, "login", login));

        SecurityUserDTO userDTO = securityUserMapper.toDto(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * Отримати список користувачів за роллю.
     *
     * @param securityRoleName назва ролі користувача.
     * @return список користувачів у форматі DTO.
     * @throws ResourceNotFoundException якщо ролі не існує.
     */
    @GetMapping("/security-role/{securityRoleName}")
    public ResponseEntity<List<SecurityUserDTO>> getBySecurityRole(@PathVariable String securityRoleName) throws ResourceNotFoundException {

        if (EnumUtils.findEnumInsensitiveCase(SecurityRole.class, securityRoleName) == null) {
            throw new ResourceNotFoundException(ENTITY_NAME, "SecurityRoleName", securityRoleName);
        }

        List<SecurityUserDTO> users = securityUserMapper.toDtoList(securityUserService.findBySecurityRole(SecurityRole.valueOf(securityRoleName)));

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Отримати список усіх ролей користувачів.
     *
     * @return список назв ролей.
     */
    @GetMapping("/security-roles")
    public ResponseEntity<List<String>> getRoles() {
        List<String> securityRoles = Arrays.stream(SecurityRole.values()).sorted().map(SecurityRole::name).toList();
        return new ResponseEntity<>(securityRoles, HttpStatus.OK);
    }

    /**
     * Отримати роль поточного користувача.
     *
     * @return роль поточного користувача або статус FORBIDDEN, якщо користувач не аутентифікований.
     */
    @GetMapping("/current-role")
    public ResponseEntity<String> getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            if (!authorities.isEmpty()) {
                String role = authorities.iterator().next().getAuthority();
                return new ResponseEntity<>(role, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     * Отримати деталі поточного користувача.
     *
     * @param userDetails дані поточного користувача (з аутентифікації).
     * @return деталі користувача.
     */
    @GetMapping("/current-user")
    public ResponseEntity<UserDetails> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

    /**
     * Додати нового користувача.
     *
     * @param userDto DTO нового користувача.
     * @return створений користувач у форматі DTO.
     * @throws ResourceExistException якщо ID вже існує.
     */
    @PostMapping
    public ResponseEntity<SecurityUserDTO> add(@RequestBody SecurityUserDTO userDto) {

        if (userDto.getId() != null) {
            throw new ResourceExistException(ENTITY_NAME + "Id", userDto.getId().toString());
        }

        SecurityUser user = securityUserMapper.toEntity(userDto);

        SecurityUserDTO savedUser = securityUserMapper.toDto(securityUserService.save(user));

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
            securityUserService.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(ENTITY_NAME, "id", id.toString());
        }
        return ResponseEntity.ok(ENTITY_NAME + " deleted successfully!.");
    }

    /**
     * Оновити дані користувача.
     *
     * @param id ідентифікатор користувача.
     * @param userDTO нові дані користувача.
     * @return оновлений користувач у форматі DTO.
     * @throws IdMismatchException якщо ID у DTO не збігається з переданим ID.
     * @throws ResourceNotFoundException якщо користувача не знайдено.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SecurityUserDTO> updateUser(@PathVariable Long id, @RequestBody SecurityUserDTO userDTO) {

        if (!userDTO.getId().equals(id)) {
            throw new IdMismatchException("SecurityUser", id.toString(), userDTO.getId().toString());
        }

        securityUserService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, "id", id.toString()));

        SecurityUser user = securityUserMapper.toEntity(userDTO);

        SecurityUserDTO savedUser = securityUserMapper.toDto(securityUserService.save(user));

        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }
}
