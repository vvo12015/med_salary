package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.TimeSheet;
import net.vrakin.medsalary.dto.TimeSheetDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class TimeSheetMapper implements BaseMapper<TimeSheet, TimeSheetDTO> {

    public TimeSheetDTO toDto(TimeSheet entity){
        TimeSheetDTO dto = new TimeSheetDTO();
        dto.setId(entity.getId());
        dto.setPlanTime(entity.getPlanTime());
        dto.setFactTime(entity.getFactTime());
        dto.setStaffListRecordId(entity.getStaffListRecordId());

        return dto;
    }

    public abstract TimeSheet toEntity(TimeSheetDTO dto);

}