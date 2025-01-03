package net.vrakin.medsalary.dto;

import lombok.*;

import java.util.List;

/**
 * DTO (Data Transfer Object) для сутності User.
 *
 * Використовується для передачі даних про користувачів між рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTO {

    /**
     * Унікальний ідентифікатор користувача.
     */
    private Long id;

    /**
     * Ім'я користувача.
     */
    private String name;

    /**
     * Індивідуальний податковий номер (ІПН) користувача.
     */
    private String ipn;

    /**
     * Список записів штатного розпису, пов'язаних із цим користувачем.
     */
    private List<StaffListRecordDTO> staffListRecords;

    /**
     * Статус DTO (наприклад, створений, редагований, зчитаний із файлу).
     */
    private DTOStatus status;
}
