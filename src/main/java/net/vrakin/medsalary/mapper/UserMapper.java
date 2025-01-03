package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.dto.UserDTO;
import org.mapstruct.Mapper;

/**
 * Мапер для перетворення між сутністю {@link User} та об'єктом передачі даних {@link UserDTO}.
 *
 * <p>Цей клас забезпечує двостороннє перетворення між об'єктами User та UserDTO для роботи
 * на рівні сервісів і представлень.</p>
 *
 * <h3>Призначення:</h3>
 * <ul>
 *     <li>Конвертує сутність {@link User} у DTO {@link UserDTO}.</li>
 *     <li>Конвертує DTO {@link UserDTO} у сутність {@link User}.</li>
 * </ul>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Для автоматичного мапінгу використовується MapStruct.</li>
 *     <li>Інтеграція зі Spring через параметр {@code componentModel = "spring"}.</li>
 *     <li>Містить ручну реалізацію методу для мапінгу з {@link User} у {@link UserDTO}.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class UserMapper implements BaseMapper<User, UserDTO> {

    /**
     * Конвертує сутність {@link User} у об'єкт передачі даних {@link UserDTO}.
     *
     * <p>Метод мапить основні поля сутності User у відповідні поля UserDTO:
     * <ul>
     *     <li><b>id:</b> Унікальний ідентифікатор користувача.</li>
     *     <li><b>name:</b> Ім'я користувача.</li>
     *     <li><b>ipn:</b> Індивідуальний податковий номер користувача.</li>
     * </ul>
     * </p>
     *
     * @param entity Сутність {@link User}, яку необхідно перетворити.
     * @return Об'єкт {@link UserDTO}, що відповідає переданій сутності.
     */
    @Override
    public UserDTO toDto(User entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIpn(entity.getIpn());

        return dto;
    }

    /**
     * Конвертує об'єкт передачі даних {@link UserDTO} у сутність {@link User}.
     *
     * <p>Цей метод делегує перетворення MapStruct, забезпечуючи автоматичний мапінг полів.</p>
     *
     * @param dto Об'єкт {@link UserDTO}, який потрібно перетворити.
     * @return Сутність {@link User}, що відповідає переданому DTO.
     */
    @Override
    public abstract User toEntity(UserDTO dto);
}
