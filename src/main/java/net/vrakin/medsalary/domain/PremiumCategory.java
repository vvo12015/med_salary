package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Сутність "Категорія премії" (Premium Category).
 *
 * Представляє категорію премії, яка містить назву, суму, коефіцієнт значення балів,
 * а також зв'язок із записами персоналу, що належать до цієї категорії.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "premium_category")
public class PremiumCategory {

    /**
     * Унікальний ідентифікатор категорії премії.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Назва категорії премії.
     *
     * Повинна бути унікальною і не може бути порожньою.
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * Сума премії, яка відповідає цій категорії.
     */
    @Column
    private Integer amount;

    /**
     * Коефіцієнт значення балів для цієї категорії.
     */
    @Column
    private Float coefficientPointValue;

    /**
     * Список записів персоналу (StaffListRecord), які належать до цієї категорії премії.
     *
     * Використовує відношення "один до багатьох".
     *
     * <ul>
     *     <li><b>cascade = CascadeType.ALL</b>: Усі операції з категорією премії також впливають на пов'язані записи персоналу.</li>
     *     <li><b>orphanRemoval = true</b>: Видалення категорії призводить до видалення всіх записів персоналу, пов'язаних із нею.</li>
     * </ul>
     */
    @OneToMany(mappedBy = "premiumCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffListRecord> staffListRecords;
}
