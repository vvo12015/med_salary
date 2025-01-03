package net.vrakin.medsalary.generator;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.exception.CalculateTypeNotFoundException;

/**
 * Інтерфейс для генерації результатів премій на основі даних зі штатного розкладу.
 *
 * <p>
 * Сервіс призначений для створення об'єктів {@link Result}, що містять інформацію
 * про розрахунки премій для працівників. Дані для генерації беруться зі штатного розкладу
 * {@link StaffListRecord}.
 * </p>
 *
 * <p>
 * Метод {@code generate} реалізує бізнес-логіку для обчислення значень полів
 * об'єкта {@link Result} на основі джерела {@link StaffListRecord}.
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
public interface GeneratorResultService {

    /**
     * Генерує об'єкт {@link Result} на основі даних зі штатного розкладу.
     *
     * <p>
     * В процесі генерації метод може враховувати такі аспекти:
     * <ul>
     *     <li>Розрахунок премій залежно від посади працівника.</li>
     *     <li>Перевірка даних на відповідність періоду, для якого здійснюється генерація.</li>
     *     <li>Обробка додаткових критеріїв, таких як нічні години, пакети послуг тощо.</li>
     * </ul>
     * </p>
     *
     * @param source Об'єкт {@link StaffListRecord}, який є джерелом даних для генерації.
     * @return Згенерований об'єкт {@link Result} із заповненими полями.
     * @throws Exception Якщо виникає помилка під час генерації (наприклад, відсутність потрібних даних).
     * @throws CalculateTypeNotFoundException Якщо не знайдено тип розрахунку для об'єкта.
     */
    Result generate(StaffListRecord source) throws Exception;
}
