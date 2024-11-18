package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String nameEleks;

    @Column
    private String departmentTemplateId;

    @Column
    private String departmentIsProId;

    @Column
    private String servicePackages;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffListRecord> staffListRecords;

    @Column
    private LocalDate period;

    @Override
    public String toString() {
        return "Department{" +
                "departmentIsProId='" + departmentIsProId + '\'' +
                ", departmentTemplateId='" + departmentTemplateId + '\'' +
                ", nameEleks='" + nameEleks + '\'' +
                ", name='" + name + '\'' +
                ", servicePackages" + servicePackages + '\'' +
                ", id=" + id +
                '}';
    }
}
