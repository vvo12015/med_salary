package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.SecurityUser;
import net.vrakin.medsalary.dto.SecurityUserDTO;
import org.mapstruct.Mapper;

/**
 * Mapper для перетворення між сутністю {@link SecurityUser} та DTO-об'єктом {@link SecurityUserDTO}.
 *
 * <p>Цей клас реалізує двосторонній мапінг між сутністю користувача безпеки та DTO-об'єктом за допомогою
 * бібліотеки MapStruct.</p>
 *
 * <h3>Призначення:</h3>
 * <ul>
 *     <li>Конвертація сутності {@link SecurityUser} у {@link SecurityUserDTO}.</li>
 *     <li>Конвертація {@link SecurityUserDTO} у сутність {@link SecurityUser}.</li>
 * </ul>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Використовує аннотацію MapStruct {@code @Mapper} для автоматизації мапінгу.</li>
 *     <li>Інтеграція зі Spring через параметр {@code componentModel = "spring"}.</li>
 *     <li>Автоматично забезпечує точність та ефективність під час роботи з DTO та сутностями.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class SecurityUserMapper implements BaseMapper<SecurityUser, SecurityUserDTO> {

    /**
     * Перетворює сутність {@link SecurityUser} у об'єкт передачі даних {@link SecurityUserDTO}.
     *
     * @param entity Сутність {@link SecurityUser}, яку потрібно перетворити.
     * @return Об'єкт {@link SecurityUserDTO}, що представляє передану сутність.
     */
    @Override
    public abstract SecurityUserDTO toDto(SecurityUser entity);

    /**
     * Перетворює об'єкт передачі даних {@link SecurityUserDTO} у сутність {@link SecurityUser}.
     *
     * @param dto DTO-об'єкт {@link SecurityUserDTO}, який потрібно перетворити.
     * @return Сутність {@link SecurityUser}, що відповідає переданому DTO.
     */
    @Override
    public abstract SecurityUser toEntity(SecurityUserDTO dto);
}
