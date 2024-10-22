package net.vrakin.medsalary.dto;

import lombok.*;

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

    private Float vlkTime;
}
