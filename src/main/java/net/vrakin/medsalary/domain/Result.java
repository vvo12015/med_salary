package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

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

    @Column(name = "employment_start_date")
    private LocalDate employmentStartDate;

    @Column(name = "employment")
    private Float employment;
    @Column
    private Float employmentPart;

    @Column(name = "employment_user_position_part")
    private Float employmentUserPositionPart;

    @Column(name = "hour_coefficient")
    private Float hourCoefficient;

    @Column(name = "night_hours")
    private Float nightHours;

    @Column
    private Float hospNSZU_Premium;

    @Column
    private Float amblNSZU_Premium;

    @Column
    private Float oneDaySurgeryPremium;

    @Column(name = "other_premium")
    private Float otherPremium;

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
    @Column
    private String comment;
    public Result(User user, UserPosition userPosition,
                  Department department,
                  Float employment, Float employmentPart, Float employmentUserPositionPart,
                  Float hourCoefficient,
                  Float nightHours,
                  LocalDate employmentStartDate, LocalDate period) {
        this.id = null;
        this.user = user;
        this.userPosition = userPosition;
        this.department = department;

        this.employment = employment;
        this.employmentPart = employmentPart;
        this.employmentUserPositionPart = employmentUserPositionPart;
        this.employmentStartDate = employmentStartDate;
        this.date = period;
        this.hourCoefficient = hourCoefficient;
        this.nightHours = nightHours;

        this.countEMR_stationary = 0;
        this.countEMR_ambulatory = 0;
        this.countEMR_oneDaySurgery = 0;
        this.countEMR_priorityService = 0;
        this.sumForAmlPackage = 0f;

        this.hospNSZU_Premium = 0f;
        this.amblNSZU_Premium = 0f;
        this.oneDaySurgeryPremium = 0f;
        this.otherPremium = 0f;

        this.comment = "";
    }

    public Result(StaffListRecord staffListRecord, Float employment, Float employmentPart,
                  Float employmentUserPositionPart,
                  Float hourCoefficient, Float nightHours) {

        this.id = null;
        this.staffListRecord = staffListRecord;
        this.user = staffListRecord.getUser();
        this.userPosition = staffListRecord.getUserPosition();
        this.department = staffListRecord.getDepartment();

        this.employment = employment;
        this.employmentPart = employmentPart;
        this.employmentUserPositionPart = employmentUserPositionPart;
        this.employmentStartDate = staffListRecord.getEmploymentStartDate();
        this.date = staffListRecord.getPeriod();
        this.hourCoefficient = hourCoefficient;
        this.nightHours = nightHours;

        this.countEMR_stationary = 0;
        this.countEMR_ambulatory = 0;
        this.countEMR_oneDaySurgery = 0;
        this.countEMR_priorityService = 0;
        this.sumForAmlPackage = 0f;

        this.comment = "";

        this.hospNSZU_Premium = 0f;
        this.amblNSZU_Premium = 0f;
        this.oneDaySurgeryPremium = 0f;
        this.otherPremium = 0f;
    }

    @Override
    public String toString() {
        return "Result{" +
                ", staffListRecordID=" + staffListRecord.getStaffListId() +
                ", user=" + user.getName() +
                ", userPosition=" + userPosition.getName() +
                ", MaxPoint=" + userPosition.getMaxPoint() +
                ", PointValue=" + userPosition.getPointValue() +
                ", departmentName=" + department.getName() +
                ", departmentIsProId=" + department.getDepartmentIsProId() +
                ", departmentTemplateId=" + department.getDepartmentTemplateId() +
                ", departmentNameEleks=" + department.getNameEleks() +
                ", employmentStartDate=" + employmentStartDate +
                ", employment=" + employment +
                ", employmentPart=" + employmentPart +
                ", employmentUserPositionPart=" + employmentUserPositionPart +
                ", hourCoefficient=" + hourCoefficient +
                ", nightHours=" + nightHours +
                ", hospNSZU_Premium=" + hospNSZU_Premium +
                ", amblNSZU_Premium=" + amblNSZU_Premium +
                ", oneDaySurgeryPremium=" + oneDaySurgeryPremium +
                ", otherPremium=" + otherPremium +
                ", date=" + date +
                ", countEMR_stationary=" + countEMR_stationary +
                ", sumForAmlPackage=" + sumForAmlPackage +
                ", countEMR_ambulatory=" + countEMR_ambulatory +
                ", countEMR_oneDaySurgery=" + countEMR_oneDaySurgery +
                ", countEMR_priorityService=" + countEMR_priorityService +
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

    public Optional<Float> getEmploymentSum(){
        try{
            return Optional.of(employment / employmentPart);
        }catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Float> getEmploymentPartStaffList(){
        try{
            float employmentSum = getEmploymentSum().isPresent()?getEmploymentSum().get():1;
            return Optional.of(this.employmentPart
                    * (employmentSum>1?1:employmentSum));
        }catch (Exception e) {
            return Optional.empty();
        }
    }

    public Float getBasicPremium(){
        return Objects.requireNonNullElse(this.amblNSZU_Premium, 0f) +
                Objects.requireNonNullElse(this.hospNSZU_Premium, 0f) +
                Objects.requireNonNullElse(this.oneDaySurgeryPremium, 0f) +
                Objects.requireNonNullElse(this.otherPremium, 0f);
    }
}
