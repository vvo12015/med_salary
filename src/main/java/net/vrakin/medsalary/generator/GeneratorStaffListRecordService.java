package net.vrakin.medsalary.generator;

import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.StaffListRecordDTO;

import java.time.LocalDate;

/**
 * Інтерфейс для генерації об'єкта {@link StaffListRecord} на основі даних
 * з {@link StaffListRecordDTO}.
 *
 * <p>
 * Даний сервіс використовується для створення записів штатного розкладу
 * на основі вхідних даних, таких як завантажені Excel-файли або інші джерела.
 * </p>
 *
 * <h3>Основні завдання:</h3>
 * <ul>
 *     <li>Перетворення DTO об'єкта {@link StaffListRecordDTO} в сутність {@link StaffListRecord}.</li>
 *     <li>Обробка можливих виключень під час генерації.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
public interface GeneratorStaffListRecordService {

    /**
     * Генерує об'єкт {@link StaffListRecord} на основі переданого DTO {@link StaffListRecordDTO}.
     *
     * @param source Об'єкт DTO, який містить вхідні дані для генерації запису штатного розкладу.
     * @return Згенерований об'єкт {@link StaffListRecord}.
     * @throws Exception Якщо під час генерації виникає помилка.
     */
    StaffListRecord generate(StaffListRecordDTO source) throws Exception;
}
