package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.*;
import net.vrakin.medsalary.excel.ExcelHelper;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class StaffListRecordMapper  implements BaseMapper<StaffListRecord, StaffListRecordDTO> {
    private ExcelHelper excelHelper;

    private UserMapper userMapper;

    private DepartmentMapper departmentMapper;

    private UserPositionMapper userPositionMapper;

    private PremiumCategoryMapper premiumCategoryMapper;

    @Autowired
    public void setExcelHelper(ExcelHelper excelHelper,
                               UserMapper userMapper,
                               DepartmentMapper departmentMapper,
                               UserPositionMapper userPositionMapper,
                               PremiumCategoryMapper premiumCategoryMapper) {
        this.excelHelper = excelHelper;
        this.userMapper = userMapper;
        this.departmentMapper = departmentMapper;
        this.userPositionMapper = userPositionMapper;
        this.premiumCategoryMapper = premiumCategoryMapper;
    }

    public StaffListRecordDTO toDto(StaffListRecord entity){
        StaffListRecordDTO staffListRecordDTO = new StaffListRecordDTO();
        staffListRecordDTO.setId(entity.getId());
        staffListRecordDTO.setStaffListId(entity.getStaffListId());
        staffListRecordDTO.setUser(userMapper.toDto(entity.getUser()));
        staffListRecordDTO.setDepartment(departmentMapper.toDto(entity.getDepartment()));
        staffListRecordDTO.setUserPosition(userPositionMapper.toDto(entity.getUserPosition()));
        staffListRecordDTO.setEmployment(entity.getEmployment());
        staffListRecordDTO.setPremiumCategory(premiumCategoryMapper.toDto(entity.getPremiumCategory()));
        //TODO builder
        return staffListRecordDTO;
    }

    public abstract StaffListRecord toEntity(StaffListRecordDTO dto);
}
