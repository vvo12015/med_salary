package net.vrakin.medsalary.dto;

import lombok.*;

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

    private DTOStatus status;
}
