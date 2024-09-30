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

    private LocalDate date;
}
