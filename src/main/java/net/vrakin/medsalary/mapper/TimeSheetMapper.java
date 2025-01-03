package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.TimeSheet;
import net.vrakin.medsalary.dto.TimeSheetDTO;
import org.mapstruct.Mapper;

/**
 * Мапер для перетворення між сутністю {@link TimeSheet} та об'єктом передачі даних {@link TimeSheetDTO}.
 *
 * <p>Цей клас забезпечує двостороннє перетворення між об'єктами TimeSheet та TimeSheetDTO для роботи
 * на рівні сервісів і представлень.</p>
 *
 * <h3>Призначення:</h3>
 * <ul>
 *     <li>Конвертує сутність {@link TimeSheet} у DTO {@link TimeSheetDTO}.</li>
 *     <li>Конвертує DTO {@link TimeSheetDTO} у сутність {@link TimeSheet}.</li>
 * </ul>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Для автоматичного мапінгу використовується MapStruct.</li>
 *     <li>Інтеграція зі Spring через параметр {@code componentModel = "spring"}.</li>
 *     <li>Надає ручну реалізацію для мапінгу з {@link TimeSheet} у {@link TimeSheetDTO}.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class TimeSheetMapper implements BaseMapper<TimeSheet, TimeSheetDTO> {

    /**
     * Конвертує сутність {@link TimeSheet} у об'єкт передачі даних {@link TimeSheetDTO}.
     *
     * <p>Метод формує DTO, заповнюючи всі основні поля із сутності TimeSheet. Поля, що заповнюються:
     * <ul>
     *     <li><b>id:</b> Ідентифікатор запису.</li>
     *     <li><b>planTime:</b> Плановий час роботи.</li>
     *     <li><b>factTime:</b> Фактичний час роботи.</li>
     *     <li><b>staffListRecordId:</b> Ідентифікатор запису у штатному розкладі.</li>
     *     <li><b>nightHours:</b> Кількість нічних годин.</li>
     *     <li><b>period:</b> Період (місяць).</li>
     * </ul>
     * </p>
     *
     * @param entity Сутність {@link TimeSheet}, яку необхідно перетворити.
     * @return Об'єкт {@link TimeSheetDTO}, що відповідає переданій сутності.
     */
    @Override
    public TimeSheetDTO toDto(TimeSheet entity) {
        TimeSheetDTO dto = new TimeSheetDTO();
        dto.setId(entity.getId());
        dto.setPlanTime(entity.getPlanTime());
        dto.setFactTime(entity.getFactTime());
        dto.setStaffListRecordId(entity.getStaffListRecordId());
        dto.setNightHours(entity.getNightHours());
        dto.setPeriod(entity.getPeriod());

        return dto;
    }

    /**
     * Конвертує об'єкт передачі даних {@link TimeSheetDTO} у сутність {@link TimeSheet}.
     *
     * <p>Цей метод делегує перетворення MapStruct, забезпечуючи автоматичний мапінг усіх полів.</p>
     *
     * @param dto Об'єкт {@link TimeSheetDTO}, який потрібно перетворити.
     * @return Сутність {@link TimeSheet}, що відповідає переданому DTO.
     */
    @Override
    public abstract TimeSheet toEntity(TimeSheetDTO dto);
}
