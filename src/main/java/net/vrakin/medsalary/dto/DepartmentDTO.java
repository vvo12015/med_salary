package net.vrakin.medsalary.dto;

import lombok.*;
import net.vrakin.medsalary.domain.PeriodControl;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DepartmentDTO implements PeriodControl {
    private Long id;

    private String name;

    private String departmentTemplateId;

    private String departmentIsProId;

    private String nameEleks;

    private String servicePackages;

    private DTOStatus status;

    private LocalDate period;
}
