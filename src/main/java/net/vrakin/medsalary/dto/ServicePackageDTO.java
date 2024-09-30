package net.vrakin.medsalary.dto;

import lombok.*;
import net.vrakin.medsalary.domain.ServicePackage;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ServicePackageDTO {
    private Long id;

    private String name;

    private String number;

    private ServicePackage.HospKind hospKind;

    private ServicePackage.OperationKind operationKind;

    private Boolean isCalculate;

    private DTOStatus status;
}
