package net.vrakin.medsalary.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ResultDTO {

    private String username;

    private String userPositionName;

    private Float maxPoint;

    private Float pointValue;

    private String departmentName;

    private Float hospNSZU_Premium;

    private Float amblNSZU_Premium;

    private Float employmentPart;

    private Float employment;

    private LocalDate date;

    private Integer countEMR_stationary;

    private Float sumForAmlPackage;

    private Integer countEMR_ambulatory;

    private Integer countEMR_oneDaySurgery;

    private Integer countEMR_priorityService;

    private Float vlkCoefficient;

    private Float sumForVlk;

    private Float wholeSumVlk;

    private LocalDate employmentStartDate;
}
