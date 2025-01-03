package net.vrakin.medsalary.dto;

import lombok.*;

/**
 * DTO (Data Transfer Object) для сутності UserEleks.
 *
 * Використовується для передачі даних про користувачів системи Eleks між рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserEleksDTO {

    /**
     * Унікальний ідентифікатор користувача в системі Eleks.
     */
    private Long id;

    /**
     * Логін користувача в системі Eleks.
     */
    private String login;

    /**
     * DTO об'єкт, який представляє зв'язок із користувачем основної системи.
     */
    private UserDTO userDTO;

    /**
     * Статус DTO (наприклад, створений, редагований, зчитаний із файлу).
     */
    private DTOStatus status;
}
