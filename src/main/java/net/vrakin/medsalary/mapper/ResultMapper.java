package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.dto.ResultDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class ResultMapper implements BaseMapper<Result, ResultDTO> {
    @Override
    @Mapping(target = "username", source = "user.name")
    @Mapping(target = "userPositionName", source = "userPosition.name")
    @Mapping(target = "maxPoint", source = "userPosition.maxPoint")
    @Mapping(target = "pointValue", source = "userPosition.pointValue")
    @Mapping(target = "departmentName", source = "department.nameEleks")
    public abstract ResultDTO toDto(Result entity);
}