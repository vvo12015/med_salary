package net.vrakin.medsalary.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class StaffListRecordDTO {
    public static final int FIRST_DAY_OF_MONTH = 01;

    private Long id;

    private String staffListId;

    private UserPositionDTO userPosition;

    private Long userPositionId;

    private DepartmentDTO department;

    private Long departmentId;

    private Float employment;

    private UserDTO user;

    private Long userId;

    private PremiumCategoryDTO premiumCategory;

    private String premiumCategoryName;

    private LocalDate employmentStartDate;

    private LocalDate employmentEndDate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Float salary;

    private DTOStatus status;

    public LocalDate getPeriod(){

        return this.startDate==null?null:this.startDate.toLocalDate();
    }

    public void setPeriod(LocalDate period){

        startDate = period==null?null:period.withDayOfMonth(FIRST_DAY_OF_MONTH).atTime(0,0);
    }
}
