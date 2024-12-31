package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Сутність "Відділення" (Department).
 *
 * Представляє відділення медичного закладу, включаючи його назву,
 * ідентифікатори шаблонів, послуги та зв'язок із записами персоналу.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "department")
public class Department implements PeriodControl {

    /**
     * Унікальний ідентифікатор відділення.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Назва відділення.
     */
    @Column
    private String name;

    /**
     * Альтернативна назва відділення (для системи Eleks).
     */
    @Column
    private String nameEleks;

    /**
     * Ідентифікатор шаблону відділення.
     */
    @Column
    private String departmentTemplateId;

    /**
     * Ідентифікатор IsPro для відділення.
     */
    @Column
    private String departmentIsProId;

    /**
     * Перелік пакетів послуг, які належать до цього відділення.
     */
    @Column
    private String servicePackages;

    /**
     * Список записів персоналу (StaffListRecord), що належать до цього відділення.
     *
     * Використовує відношення "один до багатьох".
     *
     * <ul>
     *     <li><b>cascade = CascadeType.ALL</b>: Усі операції з відділенням також впливають на пов'язані записи персоналу.</li>
     *     <li><b>orphanRemoval = true</b>: Видалення відділення призводить до видалення всіх записів персоналу, пов'язаних із ним.</li>
     * </ul>
     */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffListRecord> staffListRecords;

    /**
     * Період, до якого належить дане відділення.
     */
    @Column
    private LocalDate period;

    /**
     * Перевизначення методу toString для виводу інформації про відділення.
     *
     * @return Рядок, що описує відділення.
     */
    @Override
    public String toString() {
        return "Department{" +
                "departmentIsProId='" + departmentIsProId + '\'' +
                ", departmentTemplateId='" + departmentTemplateId + '\'' +
                ", nameEleks='" + nameEleks + '\'' +
                ", name='" + name + '\'' +
                ", servicePackages='" + servicePackages + '\'' +
                ", id=" + id +
                ", period=" + period +
                '}';
    }
}
