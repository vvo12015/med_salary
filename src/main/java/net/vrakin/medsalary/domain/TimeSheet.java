package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "time_sheet")
public class TimeSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String staffListRecordId;

    @Column
    private Float planTime;

    @Column
    private Float factTime;

    @Column
    private LocalDate period;

    @Override
    public String toString() {
        return "TimeSheet{" +
                "id=" + id +
                ", staffListRecordId='" + staffListRecordId + '\'' +
                ", planTime=" + planTime +
                ", factTime=" + factTime +
                '}';
    }
}
