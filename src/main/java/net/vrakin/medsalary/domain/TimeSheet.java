package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.*;

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
    private Float vlkTime;

    @Override
    public String toString() {
        return "TimeSheet{" +
                "id=" + id +
                ", staffListRecordId='" + staffListRecordId + '\'' +
                ", planTime=" + planTime +
                ", factTime=" + factTime +
                ", hourVlk=" + vlkTime +
                '}';
    }
}
