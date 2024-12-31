package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сутність "Користувач Eleks" (UserEleks).
 *
 * Представляє користувача з системи Eleks із базовою інформацією, такою як логін.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_eleks")
public class UserEleks {

    /**
     * Унікальний ідентифікатор користувача Eleks.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Логін користувача Eleks.
     */
    @Column
    private String login;

    /**
     * Перевизначення методу toString для представлення даних користувача у текстовому форматі.
     *
     * @return Рядок, що містить інформацію про користувача Eleks, включаючи його ID та логін.
     */
    @Override
    public String toString() {
        return "UserEleks{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}
