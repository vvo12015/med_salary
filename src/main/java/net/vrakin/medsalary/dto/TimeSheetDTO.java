package net.vrakin.medsalary.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TimeSheetDTO {

    private Long id;

    private String name;

    private String staffListRecordId;

    private Float planTime;

    private Float factTime;

    private LocalDate period;

    private Float nightHours;
}
