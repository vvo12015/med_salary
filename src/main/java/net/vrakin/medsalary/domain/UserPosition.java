package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Сутність "Посада користувача" (UserPosition).
 *
 * Представляє посаду співробітника з інформацією про її код, максимальну кількість балів,
 * значення одного бала, базову премію, пов'язані пакети послуг і період.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_position")
public class UserPosition implements PeriodControl {

    /**
     * Унікальний ідентифікатор посади.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Назва посади.
     */
    @Column
    private String name;

    /**
     * Код посади з системи IsPro.
     */
    @Column
    private String codeIsPro;

    /**
     * Максимальна кількість балів для цієї посади.
     */
    @Column
    private Integer maxPoint;

    /**
     * Значення одного бала для розрахунку.
     */
    @Column
    private Integer pointValue;

    /**
     * Базова премія для цієї посади.
     */
    @Column
    private Integer basicPremium;

    /**
     * Номери пакетів послуг, що пов'язані з цією посадою.
     */
    @Column
    private String servicePackageNumbers;

    /**
     * Назва посади в системі НСЗУ.
     */
    @Column
    private String nszuName;

    /**
     * Період (місяць і рік), для якого актуальна інформація про посаду.
     */
    @Column
    private LocalDate period;

    /**
     * Перевизначення методу toString для представлення даних про посаду у текстовому форматі.
     *
     * @return Рядок, що містить детальну інформацію про посаду.
     */
    @Override
    public String toString() {
        return "UserPosition{" +
                "name='" + name + '\'' +
                ", codeIsPro='" + codeIsPro + '\'' +
                ", maxPoint=" + maxPoint +
                ", pointValue=" + pointValue +
                ", basicPremium=" + basicPremium +
                ", nszuName=" + nszuName +
                ", servicePackageNumbers=" + servicePackageNumbers +
                ", period= " + period +
                '}';
    }
}
