package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.dto.ResultDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(target = "staffListId", source = "staffListRecord.staffListId")
    public abstract ResultDTO toDto(Result entity);

    public List<String> toStringList(Result result){
        List<String> listOfResult = new ArrayList<>();

        listOfResult.add(result.getStaffListRecord().getStaffListId());
        listOfResult.add(result.getUser().getName());
        listOfResult.add(result.getDepartment().getName());
        listOfResult.add(result.getDepartment().getDepartmentTemplateId());
        listOfResult.add(result.getUserPosition().getName());
        listOfResult.add(String.format(Objects.requireNonNullElse(result.getEmploymentStartDate(), "01-01-2024").toString()));
        listOfResult.add(String.format("%.2f",(result.getEmployment())));
        listOfResult.add(String.format("%.2f",result.getEmploymentPartStaffList().orElse(0f)));//TODO
        listOfResult.add(String.format("%.2f",result.getEmploymentUserPositionPart()));//TODO
        listOfResult.add(Objects.requireNonNullElse(result.getHourCoefficient(), 0).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getNightHours(), 0).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getUserPosition().getMaxPoint(), 0).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getUserPosition().getPointValue(), 0).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getBasicPremium(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getHospNSZU_Premium(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getCountEMR_stationary(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getAmblNSZU_Premium(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getSumForAmlPackage(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getCountEMR_priorityService(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getOneDaySurgeryPremium(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getCountEMR_oneDaySurgery(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getOtherPremium(), 0f).toString());
        listOfResult.add(Objects.requireNonNullElse(result.getComment(), 0f).toString());
        listOfResult.add(String.format(Objects.requireNonNullElse(result.getDate(), "01-01-2024").toString()));

        return listOfResult;
    }

    public List<String> toStringListColNameForExcel(){
        List<String> listOfResult = new ArrayList<>();

        listOfResult.add("Табельний номер");
        listOfResult.add("ПІБ працівника");
        listOfResult.add("Назва відділення");
        listOfResult.add("Код шаблона відділення");
        listOfResult.add("Назва посади");
        listOfResult.add("Дата прийому");
        listOfResult.add("Ставка поточної посади");
        listOfResult.add("Частка ставки штатки");
        listOfResult.add("Частка ставки посади");
        listOfResult.add("Коефіцієнт відпрацьованих годин");
        listOfResult.add("Кількість нічних годин");
        listOfResult.add("Максимальна кількість балів");
        listOfResult.add("Сума за бал");
        listOfResult.add("Сума премій");
        listOfResult.add("Премія за стаціонар");
        listOfResult.add("Кількість ЕМЗ в стаціонарі");
        listOfResult.add("Премія 9 пакет");
        listOfResult.add("Сума 9 пакет");
        listOfResult.add("Кількість пріоритетних послуг");
        listOfResult.add("Премія за стаціонар одного дня");
        listOfResult.add("Кількість ЕМЗ стаціонару одного дня");
        listOfResult.add("Інші премії");
        listOfResult.add("Опис");
        listOfResult.add("Дата");

        return listOfResult;
    }

    public List<String> toStringListColNameForSQL(){
        List<String> listOfResult = new ArrayList<>();

        listOfResult.add("sl_id");
        listOfResult.add("UserShortName");
        listOfResult.add("DepartmentName");
        listOfResult.add("DepartmentID");
        listOfResult.add("UserPosition");
        listOfResult.add("EmploymentStartDate");
        listOfResult.add("Employment");
        listOfResult.add("EmploymentPart");
        listOfResult.add("EmploymentUserPositionPart");
        listOfResult.add("HourCoefficient");
        listOfResult.add("NightHours");
        listOfResult.add("Point");
        listOfResult.add("PointValue");
        listOfResult.add("BasicPremium");
        listOfResult.add("HospNSZU_Premium");
        listOfResult.add("CountEMR_stationary");
        listOfResult.add("AmblNSZU_Premium");
        listOfResult.add("SumForAmlPackage");
        listOfResult.add("CountEMR_priorityService");
        listOfResult.add("OneDaySurgery");
        listOfResult.add("CountEMR_oneDaySurgery");
        listOfResult.add("OtherPremium");

        return listOfResult;
    }


}