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

    public Result(User user, UserPosition userPosition, Department department, Float employmentPart) {
        this.id = null;
        this.user = user;
        this.userPosition = userPosition;
        this.department = department;
        this.employmentPart = employmentPart;
        this.hospNSZU_Premium = 0f;
        this.amblNSZU_Premium = 0f;
        this.oneDaySurgery = 0f;
        this.date = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", user=" + user.getName() +
                ", userPosition=" + userPosition.getName() +
                ", department=" + department.getName() +
                ", hospNSZU_Premium=" + hospNSZU_Premium.toString() +
                ", amblNSZU_Premium=" + amblNSZU_Premium.toString() +
                ", oneDaySurgery=" + oneDaySurgery.toString() +
                ", date=" + date.toString() +
                '}';
    }

}
