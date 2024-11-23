package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "staff_list")
public class StaffListRecord implements PeriodControl{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String staffListId;

    @ManyToOne
    @JoinColumn(name = "user_position_id")
    private UserPosition userPosition;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column
    private Float employment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "premium_category_id")
    private PremiumCategory premiumCategory;

    @Column
    private LocalDate employmentStartDate;

    @Column
    private LocalDate employmentEndDate;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private Float salary;

    @Override
    public LocalDate getPeriod() {
        return startDate.toLocalDate();
    }

    @Override
    public void setPeriod(LocalDate period) {
        startDate = period.atTime(0, 0);
    }



    @Override
    public String toString() {
        return "StaffListRecord{" +
                "id=" + id +
                ", staffListId='" + staffListId + '\'' +
                ", userPositionId=" + userPosition.getId() +
                ", departmentNameId=" + department.getId() +
                ", employment=" + employment +
                ", user.Id=" + user.getId() +
                ", employmentStartDate=" + employmentStartDate +
                ", employmentEndDate=" + employmentEndDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
