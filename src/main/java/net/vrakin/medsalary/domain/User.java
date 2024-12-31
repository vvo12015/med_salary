package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Сутність "Користувач" (User).
 *
 * Представляє користувача системи, включаючи інформацію про ім'я, ідентифікаційний код платника податків (ІПН),
 * а також пов'язані записи у штатному розписі.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    /**
     * Унікальний ідентифікатор користувача.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ім'я користувача.
     */
    @Column
    private String name;

    /**
     * Ідентифікаційний код платника податків (ІПН) користувача.
     */
    @Column(name = "ipn", length = 15)
    private String ipn;

    /**
     * Список записів у штатному розписі, пов'язаних із користувачем.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffListRecord> staffListRecords;

    /**
     * Перевизначення методу toString для представлення даних користувача у текстовому форматі.
     *
     * @return Рядок, що містить інформацію про користувача, включаючи його ID, ім'я, ІПН
     *         і список пов'язаних записів у штатному розписі.
     */
    @Override
    public String toString() {
        StringBuilder staffListRecordIds = new StringBuilder();
        if (staffListRecords != null) {
            staffListRecordIds.append("StaffListRecord:");
            staffListRecords.forEach(sl -> staffListRecordIds.append(sl.getStaffListId()).append(","));
        }
        return "User{" +
                "id=" + id +
                ", name='" + name + "', " +
                staffListRecordIds +
                ", " + ipn +
                '}';
    }
}
