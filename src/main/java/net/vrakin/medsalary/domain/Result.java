package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="result")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_position_id")
    private UserPosition userPosition;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column
    private Float hospNSZU_Premium;

    @Column
    private Float amblNSZU_Premium;

    @Column
    private Float oneDaySurgery;

    @Column
    private Float employmentPart;

    @Column
    private LocalDate date;

    private Integer countEMR_stationary;

    private Float sumForAmlPackage;

    private Integer countEMR_ambulatory;

    private Integer countEMR_oneDaySurgery;

    private Integer countEMR_priorityService;

    private Float employment;

    private Float hourCoefficient;

    private Float vlkCoefficient;

    private Float sumForVlk;

    private Float wholeSumVlk;

    private LocalDate employmentStartDate;

    public Result(User user, UserPosition userPosition,
                  Department department,
                  Float employment, Float employmentPart,
                  Float hourCoefficient, Float vlkCoefficient, LocalDate employmentStartDate) {
        this.id = null;
        this.user = user;
        this.userPosition = userPosition;
        this.department = department;
        this.employment = employment;
        this.employmentPart = employmentPart;
        this.hospNSZU_Premium = 0f;
        this.amblNSZU_Premium = 0f;
        this.oneDaySurgery = 0f;
        this.sumForVlk = 0f;
        this.date = LocalDate.now();
        this.hourCoefficient = hourCoefficient;
        this.vlkCoefficient = vlkCoefficient;
        this.employmentStartDate = employmentStartDate;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", user=" + user.getName() +
                ", userPosition=" + userPosition.getName() +
                ", department=" + department.getName() +
                ", hospNSZU_Premium=" + hospNSZU_Premium +
                ", amblNSZU_Premium=" + amblNSZU_Premium +
                ", oneDaySurgery=" + oneDaySurgery +
                ", employmentPart=" + employmentPart +
                ", date=" + date +
                ", countEMR_stationary=" + countEMR_stationary +
                ", sumForAmlPackage=" + sumForAmlPackage +
                ", countEMR_ambulatory=" + countEMR_ambulatory +
                ", countEMR_oneDaySurgery=" + countEMR_oneDaySurgery +
                ", countEMR_priorityService=" + countEMR_priorityService +
                ", employment=" + employment +
                ", hourCoefficient=" + hourCoefficient +
                ", vlkCoefficient=" + vlkCoefficient +
                '}';
    }
}
