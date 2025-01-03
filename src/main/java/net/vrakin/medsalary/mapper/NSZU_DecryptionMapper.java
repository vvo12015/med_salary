package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.dto.NszuDecryptionDTO;
import org.mapstruct.Mapper;

/**
 * Мапер для перетворення між сутністю {@link NszuDecryption} та об'єктом передачі даних {@link NszuDecryptionDTO}.
 *
 * <p>Використовується для конвертації даних із бази даних у DTO для представлення або обробки,
 * а також для перетворення DTO у сутності при збереженні в базу даних.</p>
 *
 * <p>Реалізує інтерфейс {@link BaseMapper}, який забезпечує стандартизований підхід до перетворення даних.</p>
 *
 * <p>Для автоматизації процесу перетворення використовується бібліотека MapStruct.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class NSZU_DecryptionMapper implements BaseMapper<NszuDecryption, NszuDecryptionDTO> {

    /**
     * Перетворює сутність {@link NszuDecryption} у об'єкт {@link NszuDecryptionDTO}.
     *
     * <p>Цей метод автоматично мапить всі відповідні поля між сутністю та DTO.</p>
     *
     * @param entity Сутність {@link NszuDecryption}, яку необхідно перетворити.
     * @return Об'єкт {@link NszuDecryptionDTO}, що відповідає переданій сутності.
     */
    public abstract NszuDecryptionDTO toDto(NszuDecryption entity);

    /**
     * Перетворює об'єкт {@link NszuDecryptionDTO} у сутність {@link NszuDecryption}.
     *
     * <p>Цей метод використовується для перетворення DTO в сутність при збереженні або оновленні даних у базі.</p>
     *
     * @param dto Об'єкт {@link NszuDecryptionDTO}, який необхідно перетворити.
     * @return Сутність {@link NszuDecryption}, що відповідає переданому DTO.
     */
    public abstract NszuDecryption toEntity(NszuDecryptionDTO dto);
}
