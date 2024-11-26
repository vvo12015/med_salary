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

        StaffListRecordDTO staffListRecordDTO = StaffListRecordDTO.builder()
                .id(entity.getId())
                .staffListId(entity.getStaffListId())
                .userPosition(userPositionMapper.toDto(entity.getUserPosition()))
                .userPositionId(entity.getUserPosition().getId())
                .department(departmentMapper.toDto(entity.getDepartment()))
                .departmentId(entity.getDepartment().getId())
                .employment(entity.getEmployment())
                .user(userMapper.toDto(entity.getUser()))
                .userId(entity.getUser().getId())
                .premiumCategory(premiumCategoryMapper.toDto(entity.getPremiumCategory()))
                .premiumCategoryName(entity.getPremiumCategory().getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .salary(entity.getSalary())
                .build();

        return staffListRecordDTO;
    }

    public StaffListRecord toEntity(StaffListRecordDTO dto) throws Exception {
        return generatorStaffListRecordService.generate(dto);
    }
}
