package net.vrakin.medsalary.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PremiumCategoryDTO {
    private Long id;

    private String name;

    private Integer amount;

    private List<StaffListRecordDTO> staffListRecords;

    private DTOStatus status;
}
