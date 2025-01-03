package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.UserPositionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Мапер для перетворення між сутністю {@link UserPosition} та об'єктом передачі даних {@link UserPositionDTO}.
 *
 * <p>Цей клас забезпечує двостороннє перетворення між об'єктами UserPosition та UserPositionDTO для роботи
 * на рівні сервісів і представлень.</p>
 *
 * <h3>Призначення:</h3>
 * <ul>
 *     <li>Конвертує сутність {@link UserPosition} у DTO {@link UserPositionDTO}.</li>
 *     <li>Конвертує DTO {@link UserPositionDTO} у сутність {@link UserPosition}.</li>
 * </ul>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Для автоматичного мапінгу використовується MapStruct.</li>
 *     <li>Інтеграція зі Spring через параметр {@code componentModel = "spring"}.</li>
 *     <li>Забезпечується мапінг періоду між сутністю та DTO через поле {@code period}.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class UserPositionMapper implements BaseMapper<UserPosition, UserPositionDTO> {

    /**
     * Конвертує сутність {@link UserPosition} у об'єкт передачі даних {@link UserPositionDTO}.
     *
     * <p>Метод автоматично мапить усі поля, включаючи поле {@code period}.</p>
     *
     * @param entity Сутність {@link UserPosition}, яку необхідно перетворити.
     * @return Об'єкт {@link UserPositionDTO}, що відповідає переданій сутності.
     */
    @Override
    @Mapping(target = "period", source = "period")
    public abstract UserPositionDTO toDto(UserPosition entity);

    /**
     * Конвертує об'єкт передачі даних {@link UserPositionDTO} у сутність {@link UserPosition}.
     *
     * <p>Метод автоматично мапить усі поля, включаючи поле {@code period}.</p>
     *
     * @param dto Об'єкт {@link UserPositionDTO}, який потрібно перетворити.
     * @return Сутність {@link UserPosition}, що відповідає переданому DTO.
     */
    @Override
    @Mapping(target = "period", source = "period")
    public abstract UserPosition toEntity(UserPositionDTO dto);
}
