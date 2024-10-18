package net.vrakin.medsalary.dto;

import jakarta.persistence.*;
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
}
