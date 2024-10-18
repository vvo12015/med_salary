package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.*;
import net.vrakin.medsalary.generator.GeneratorStaffListRecordService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class StaffListRecordMapper  implements BaseMapper<StaffListRecord, StaffListRecordDTO> {

    private UserMapper userMapper;

    private DepartmentMapper departmentMapper;

    private UserPositionMapper userPositionMapper;

    private PremiumCategoryMapper premiumCategoryMapper;

    private GeneratorStaffListRecordService generatorStaffListRecordService;

    @Autowired
    public void setExcelHelper(UserMapper userMapper,
                               DepartmentMapper departmentMapper,
                               UserPositionMapper userPositionMapper,
                               PremiumCategoryMapper premiumCategoryMapper,
                               GeneratorStaffListRecordService generatorStaffListRecordService) {
        this.userMapper = userMapper;
        this.departmentMapper = departmentMapper;
        this.userPositionMapper = userPositionMapper;
        this.premiumCategoryMapper = premiumCategoryMapper;
        this.generatorStaffListRecordService = generatorStaffListRecordService;
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

    public StaffListRecord toEntity(StaffListRecordDTO dto) throws Exception {
        return generatorStaffListRecordService.generate(dto);
    }
}
