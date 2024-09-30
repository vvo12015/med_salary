package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_package")
public class ServicePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String number;

    @Column
    private HospKind hospKind;

    @Column
    private OperationKind operationKind;

    @Column
    private Boolean isCalculate;

    public enum HospKind{
        STATIONARY,
        AMBULATORY,
        MIXED
    }

    public enum OperationKind{
        OPERATION,
        NO_OPERATION,
        MIXED
    }

}
