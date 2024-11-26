package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.dto.DTOStatus;
import net.vrakin.medsalary.dto.DepartmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class DepartmentMapper implements BaseMapper<Department, DepartmentDTO> {
    @Override
    public DepartmentDTO toDto(Department entity){
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(entity.getId());
        departmentDTO.setDepartmentIsProId(entity.getDepartmentIsProId());
        departmentDTO.setDepartmentTemplateId(entity.getDepartmentTemplateId());
        departmentDTO.setName(entity.getName());
        departmentDTO.setNameEleks(entity.getNameEleks());
        departmentDTO.setPeriod(entity.getPeriod());
        departmentDTO.setStatus(DTOStatus.FROM_ENTITY);

        return departmentDTO;
    }

    @Override
    @Mapping(target = "period", source = "period")
    public abstract Department toEntity(DepartmentDTO dto);
}