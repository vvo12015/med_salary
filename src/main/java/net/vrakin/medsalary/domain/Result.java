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
public class Result implements PeriodControl{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "stafflist_id")
    private StaffListRecord staffListRecord;

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

    @Column(name = "countemr_stationary")
    private Integer countEMR_stationary;

    @Column(name = "sum_for_aml_package")
    private Float sumForAmlPackage;

    @Column(name = "countemr_ambulatory")
    private Integer countEMR_ambulatory;

    @Column(name = "countemr_one_day_surgery")
    private Integer countEMR_oneDaySurgery;

    @Column(name = "countemr_priority_service")
    private Integer countEMR_priorityService;

    @Column(name = "employment")
    private Float employment;

    @Column(name = "hour_coefficient")
    private Float hourCoefficient;

    @Column(name = "other_premium")
    private Float otherPremium;

    @Column(name = "employment_start_date")
    private LocalDate employmentStartDate;

    @Column
    private String comment;
    public Result(User user, UserPosition userPosition,
                  Department department,
                  Float employment, Float employmentPart,
                  Float hourCoefficient,
                  LocalDate employmentStartDate, LocalDate period) {
        this.id = null;
        this.user = user;
        this.userPosition = userPosition;
        this.department = department;

        this.employment = employment;
        this.employmentPart = employmentPart;
        this.employmentStartDate = employmentStartDate;
        this.date = period;
        this.hourCoefficient = hourCoefficient;

        this.countEMR_stationary = 0;
        this.countEMR_ambulatory = 0;
        this.countEMR_oneDaySurgery = 0;
        this.countEMR_priorityService = 0;
        this.sumForAmlPackage = 0f;

        this.hospNSZU_Premium = 0f;
        this.amblNSZU_Premium = 0f;
        this.oneDaySurgery = 0f;
        this.otherPremium = 0f;

        this.comment = "";
    }

    public Result(StaffListRecord staffListRecord, Float employment, Float employmentPart, Float hourCoefficient) {

        this.id = null;
        this.staffListRecord = staffListRecord;
        this.user = staffListRecord.getUser();
        this.userPosition = staffListRecord.getUserPosition();
        this.department = staffListRecord.getDepartment();

        this.employment = employment;
        this.employmentPart = employmentPart;
        this.employmentStartDate = staffListRecord.getEmploymentStartDate();
        this.date = staffListRecord.getPeriod();
        this.hourCoefficient = hourCoefficient;

        this.countEMR_stationary = 0;
        this.countEMR_ambulatory = 0;
        this.countEMR_oneDaySurgery = 0;
        this.countEMR_priorityService = 0;
        this.sumForAmlPackage = 0f;

        this.comment = "";

        this.hospNSZU_Premium = 0f;
        this.amblNSZU_Premium = 0f;
        this.oneDaySurgery = 0f;
        this.otherPremium = 0f;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", user=" + user.getName() +
                ", staffListRecordID=" + staffListRecord.getStaffListId() +
                ", userPosition=" + userPosition.getName() +
                ", userPosition=" + userPosition.getMaxPoint() +
                ", userPosition=" + userPosition.getPointValue() +
                ", departmentName=" + department.getName() +
                ", departmentIsProId=" + department.getDepartmentIsProId() +
                ", departmentTemplateId=" + department.getDepartmentTemplateId() +
                ", departmentNameEleks=" + department.getNameEleks() +
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
                ", otherPremium=" + otherPremium +
                ", employmentStartDate=" + employmentStartDate +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public LocalDate getPeriod() {
        return date;
    }

    @Override
    public void setPeriod(LocalDate period) {
        date = period;
}
}
