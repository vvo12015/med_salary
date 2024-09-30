package net.vrakin.medsalary.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DepartmentDTO {
    private Long id;

    private String name;

    private String departmentTemplateId;

    private String departmentIsProId;

    private String nameEleks;

    private String servicePackages;

    private DTOStatus status;
}
