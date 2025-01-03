package net.vrakin.medsalary.dto;

import lombok.*;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) для сутності UserPosition.
 *
 * Використовується для передачі даних про посади користувачів між рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserPositionDTO {

    /**
     * Унікальний ідентифікатор посади.
     */
    private Long id;

    /**
     * Назва посади.
     */
    private String name;

    /**
     * Код посади в системі IsPro.
     */
    private String codeIsPro;

    /**
     * Максимально можливі бали для посади.
     */
    private Integer maxPoint;

    /**
     * Вартість одного бала для посади.
     */
    private Integer pointValue;

    /**
     * Базова премія для посади.
     */
    private Integer basicPremium;

    /**
     * Назва посади у системі NSZU.
     */
    private String nszuName;

    /**
     * Список номерів пакетів послуг, пов'язаних із посадою.
     */
    private String servicePackageNumbers;

    /**
     * Період (місяць і рік), до якого належить запис посади.
     */
    private LocalDate period;
}
