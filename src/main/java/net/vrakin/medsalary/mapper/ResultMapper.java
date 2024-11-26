package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.dto.ResultDTO;
import net.vrakin.medsalary.service.service_package_handler.CalculateVlk;
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

        listOfResult.add(result.getUser().getName());
        listOfResult.add(result.getDepartment().getName());
        listOfResult.add(result.getDepartment().getDepartmentTemplateId());
        listOfResult.add(result.getUserPosition().getName());
        listOfResult.add(String.format(Objects.requireNonNullElse(result.getEmploymentStartDate(), "01-01-2024").toString()));
        listOfResult.add(Objects.requireNonNullElse(result.getHourCoefficient(), 0).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getUserPosition().getMaxPoint(), 0).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getUserPosition().getPointValue(), 0).toString());
        listOfResult.add(String.format("%.2f",(result.getEmployment())));
        listOfResult.add(String.format("%.2f",result.getEmploymentPart()));
        listOfResult.add(String.format("%.2f",(result.getEmploymentPart()* result.getEmployment())));
        listOfResult.add(Objects.requireNonNullElse(result.getHospNSZU_Premium(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getCountEMR_stationary(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getAmblNSZU_Premium(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getSumForAmlPackage(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getCountEMR_priorityService(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getOneDaySurgery(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getCountEMR_oneDaySurgery(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getOtherPremium(), 0f).toString());

        return listOfResult;
    }

    public List<String> toStringListColNames(){
        List<String> listOfResult = new ArrayList<>();

        listOfResult.add("ПІБ працівника");
        listOfResult.add("Назва відділення");
        listOfResult.add("Код шаблона відділення");
        listOfResult.add("Назва посади");
        listOfResult.add("Дата прийому");
        listOfResult.add("Коефіцієнт відпрацьованих годин");
        listOfResult.add("Максимальна кількість балів");
        listOfResult.add("Сума за бал");
        listOfResult.add("Сума ставок");
        listOfResult.add("Частка ставки");
        listOfResult.add("Ставка поточної посади");
        listOfResult.add("Премія за стаціонар");
        listOfResult.add("Кількість ЕМЗ в стаціонарі");
        listOfResult.add("Премія 9 пакет");
        listOfResult.add("Сума 9 пакет");
        listOfResult.add("Кількість пріоритетних послуг");
        listOfResult.add("Премія за стаціонар одного дня");
        listOfResult.add("Кількість ЕМЗ стаціонару одного дня");
        listOfResult.add("Коефіцієнт по ВЛК");
        listOfResult.add("Премія по ВЛК");
        listOfResult.add("Сума по ВЛК");
        listOfResult.add("Дата");
        listOfResult.add("Всі інші премії");

        return listOfResult;
    }
}