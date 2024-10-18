package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.dto.ResultDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<String> toStringList(Result result){
        List<String> listOfResult = new ArrayList<>();

        listOfResult.add(Objects.toString(result.getId()));
        listOfResult.add(result.getUser().getName());
        listOfResult.add(result.getDepartment().getName());
        listOfResult.add(result.getUserPosition().getName());
        listOfResult.add(result.getEmploymentPart().toString());
        listOfResult.add(result.getHospNSZU_Premium().toString());
        listOfResult.add(result.getAmblNSZU_Premium().toString());
        listOfResult.add(result.getOneDaySurgery().toString());
        listOfResult.add(result.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        return listOfResult;
    }
}