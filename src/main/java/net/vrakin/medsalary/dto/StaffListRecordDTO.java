package net.vrakin.medsalary.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class StaffListRecordDTO {
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

    private LocalDateTime employmentStartDate;

    private LocalDateTime employmentEndDate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Float Salary;

    private DTOStatus status;
}
