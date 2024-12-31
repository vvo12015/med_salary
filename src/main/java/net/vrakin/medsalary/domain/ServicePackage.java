package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сутність "Пакет послуг" (ServicePackage).
 *
 * Представляє медичний пакет послуг із деталями, такими як назва, номер,
 * тип госпіталізації, тип операції та статус обчислення.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_package")
public class ServicePackage {

    /**
     * Унікальний ідентифікатор пакету послуг.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Назва пакету послуг.
     */
    @Column
    private String name;

    /**
     * Номер пакету послуг.
     */
    @Column
    private String number;

    /**
     * Тип госпіталізації, пов'язаний із пакетом послуг.
     * Може бути значенням з {@link HospKind}.
     */
    @Column
    private HospKind hospKind;

    /**
     * Тип операції, пов'язаний із пакетом послуг.
     * Може бути значенням з {@link OperationKind}.
     */
    @Column
    private OperationKind operationKind;

    /**
     * Прапорець, що вказує, чи включений цей пакет у розрахунки.
     */
    @Column
    private Boolean isCalculate;

    /**
     * Перерахунок (enum) "Тип госпіталізації" (HospKind).
     *
     * Визначає, чи є послуга стаціонарною, амбулаторною чи змішаною.
     */
    public enum HospKind {
        /**
         * Стаціонарна допомога.
         */
        STATIONARY,

        /**
         * Амбулаторна допомога.
         */
        AMBULATORY,

        /**
         * Змішаний тип допомоги.
         */
        MIXED
    }

    /**
     * Перерахунок (enum) "Тип операції" (OperationKind).
     *
     * Визначає, чи включає послуга операції, чи ні, або має змішаний тип.
     */
    public enum OperationKind {
        /**
         * Пакет включає операції.
         */
        OPERATION,

        /**
         * Пакет не включає операції.
         */
        NO_OPERATION,

        /**
         * Змішаний тип операцій.
         */
        MIXED
    }

    /**
     * Повертає повну назву пакету, яка включає номер і назву.
     *
     * @return Повна назва пакету у форматі "Номер Назва".
     */
    public String getFullName() {
        return String.format("%s %s", this.number, this.name);
    }
}
