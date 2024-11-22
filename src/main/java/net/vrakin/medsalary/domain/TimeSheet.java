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
public class TimeSheet implements PeriodControl{

    public static final int NIGHT_TO_FULL_DAY_COEFFICIENT = 3;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String staffListRecordId;

    @Column
    private Float planTime;

    @Column
    private Float factTime;

    @Column(name = "night_hours")
    private Float nightHours;

    @Column
    private LocalDate period;
    
    public float getHourCoefficient(){
        return factTime / planTime;
    }
    
    public boolean getUgrency(){
        return
                (nightHours* NIGHT_TO_FULL_DAY_COEFFICIENT) > (factTime/2);
    }

    @Override
    public String toString() {
        return "TimeSheet{" +
                "id=" + id +
                ", staffListRecordId='" + staffListRecordId + '\'' +
                ", planTime=" + planTime +
                ", factTime=" + factTime +
                ", nightHours=" + nightHours +
                ", period=" + period.toString() +
                '}';
    }
}
