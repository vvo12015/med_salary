package net.vrakin.medsalary.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserPositionDTO {
    private Long id;

    private String name;

    private String codeIsPro;

    private Integer maxPoint;

    private Integer pointValue;

    private Integer basicPremium;

    private String nszuName;

    private String servicePackageNumbers;

    private LocalDate period;
}
