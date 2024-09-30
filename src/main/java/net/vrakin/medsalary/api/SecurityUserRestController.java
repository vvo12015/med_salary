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

@RestController
@RequestMapping("/api/s-users")
public class SecurityUserRestController {

    public static final String ENTITY_NAME = "SecurityUser";
    private final SecurityUserService securityUserService;
    private final SecurityUserMapper securityUserMapper;

    @Autowired
    public SecurityUserRestController(SecurityUserService securityUserService,
                                      SecurityUserMapper securityUserMapper) {
        this.securityUserService = securityUserService;
        this.securityUserMapper = securityUserMapper;
    }

    @GetMapping
    public ResponseEntity<List<SecurityUserDTO>> getAll() {
        List<SecurityUserDTO> users = securityUserMapper.toDtoList(securityUserService.findAll());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecurityUserDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        SecurityUser user = securityUserService.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(ENTITY_NAME, "id", id.toString()));

        SecurityUserDTO userDTO = securityUserMapper.toDto(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<SecurityUserDTO> getByLogin(@PathVariable String login) throws ResourceNotFoundException {
        SecurityUser user = securityUserService.findByLogin(login)
                .orElseThrow(()->new ResourceNotFoundException(ENTITY_NAME, "login", login));

        SecurityUserDTO userDTO = securityUserMapper.toDto(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/security-role/{securityRoleName}")
    public ResponseEntity<List<SecurityUserDTO>> getBySecurityRole(@PathVariable String securityRoleName) throws ResourceNotFoundException {

        if (EnumUtils.findEnumInsensitiveCase(SecurityRole.class, securityRoleName) == null) {
            throw new ResourceNotFoundException(ENTITY_NAME, "SecurityRoleName", securityRoleName);
        }

        List<SecurityUserDTO> users = securityUserMapper.toDtoList(securityUserService.findBySecurityRole(SecurityRole.valueOf(securityRoleName)));

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/security-roles")
    public ResponseEntity<List<String>> getRoles(){

        List<String> securityRoles = Arrays.stream(SecurityRole.values()).sorted().map(SecurityRole::name).toList();

        return new ResponseEntity<>(securityRoles, HttpStatus.OK);
    }

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

    @GetMapping("/current-user")
    public ResponseEntity<UserDetails> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SecurityUserDTO> add(@RequestBody SecurityUserDTO userDto) {

        if (userDto.getId() != null) {
            throw new ResourceExistException(ENTITY_NAME + "Id", userDto.getId().toString());
        }

        SecurityUser user = securityUserMapper.toEntity(userDto);

        SecurityUserDTO savedUser = securityUserMapper.toDto(securityUserService.save(user));

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            securityUserService.deleteById(id);
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(ENTITY_NAME, "id", id.toString());
        }
        return ResponseEntity.ok(ENTITY_NAME + " deleted successfully!.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<SecurityUserDTO> updateUser(@PathVariable Long id, @RequestBody SecurityUserDTO userDTO) {

        if (!userDTO.getId().equals(id)){
            throw new IdMismatchException("SecurityUser", id.toString(), userDTO.getId().toString());
        }

        securityUserService.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(ENTITY_NAME, "id", id.toString()));


        SecurityUser user = securityUserMapper.toEntity(userDTO);


        SecurityUserDTO savedUser = securityUserMapper.toDto(securityUserService.save(user));

        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }
}
