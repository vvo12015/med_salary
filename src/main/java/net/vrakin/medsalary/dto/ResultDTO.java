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

    private Long id;

    private String username;

    private String staffListId;

    private String userPositionName;

    private String departmentName;

    private LocalDate employmentStartDate;

    private Float employment;

    private Float employmentPart;

    private Float hourCoefficient;

    private Float nightHours;

    private Float maxPoint;

    private Float pointValue;

    private Float hospNSZU_Premium;

    private Float amblNSZU_Premium;

    private Float oneDaySurgeryPremium;

    private Float otherPremium;

    private LocalDate date;

    private Integer countEMR_stationary;

    private Float sumForAmlPackage;

    private Integer countEMR_ambulatory;

    private Integer countEMR_oneDaySurgery;

    private Integer countEMR_priorityService;

    private String comment;

}
