package net.vrakin.medsalary.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTO {
    private Long id;
    private String name;
    private String ipn;

    private List<StaffListRecordDTO> staffListRecords;

    private DTOStatus status;
}
