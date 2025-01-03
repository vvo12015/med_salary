package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.dto.ServicePackageDTO;
import org.mapstruct.Mapper;

/**
 * Mapper для перетворення між сутністю {@link ServicePackage} та DTO-об'єктом {@link ServicePackageDTO}.
 *
 * <p>Цей клас реалізує двостороннє перетворення між сутністю пакету послуг та її DTO-репрезентацією
 * за допомогою бібліотеки MapStruct.</p>
 *
 * <h3>Призначення:</h3>
 * <ul>
 *     <li>Конвертація сутності {@link ServicePackage} у {@link ServicePackageDTO}.</li>
 *     <li>Конвертація {@link ServicePackageDTO} у сутність {@link ServicePackage}.</li>
 * </ul>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Використовує аннотацію MapStruct {@code @Mapper} для автоматизації мапінгу.</li>
 *     <li>Інтегрується зі Spring через параметр {@code componentModel = "spring"}.</li>
 *     <li>Реалізує інтерфейс {@link BaseMapper} для спрощення мапінгу та підтримки консистентності коду.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class ServicePackageMapper implements BaseMapper<ServicePackage, ServicePackageDTO> {

    /**
     * Перетворює сутність {@link ServicePackage} у об'єкт передачі даних {@link ServicePackageDTO}.
     *
     * @param entity Сутність {@link ServicePackage}, яку потрібно перетворити.
     * @return Об'єкт {@link ServicePackageDTO}, що представляє передану сутність.
     */
    @Override
    public abstract ServicePackageDTO toDto(ServicePackage entity);

    /**
     * Перетворює об'єкт передачі даних {@link ServicePackageDTO} у сутність {@link ServicePackage}.
     *
     * @param dto DTO-об'єкт {@link ServicePackageDTO}, який потрібно перетворити.
     * @return Сутність {@link ServicePackage}, що відповідає переданому DTO.
     */
    @Override
    public abstract ServicePackage toEntity(ServicePackageDTO dto);
}
