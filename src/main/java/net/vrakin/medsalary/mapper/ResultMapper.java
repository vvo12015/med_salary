package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.dto.ResultDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mapper for converting between {@link Result} entity and {@link ResultDTO}.
 *
 * <p>This mapper is implemented using MapStruct for seamless conversion between the domain entity
 * and its corresponding Data Transfer Object (DTO).</p>
 *
 * <p>Additionally, this mapper includes utility methods for exporting {@link Result} objects
 * as lists of strings suitable for formats like Excel and SQL.</p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *     <li>Maps nested properties like {@code user.name}, {@code userPosition.name}, etc.</li>
 *     <li>Includes null-safety checks with fallback values.</li>
 *     <li>Supports generating column names and values for export to Excel and SQL.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class ResultMapper implements BaseMapper<Result, ResultDTO> {

    /**
     * Converts a {@link Result} entity to a {@link ResultDTO}.
     *
     * <p>Uses MapStruct to automatically map fields. Additionally, it maps nested properties:
     * <ul>
     *     <li>{@code username} is mapped from {@code user.name}.</li>
     *     <li>{@code userPositionName} is mapped from {@code userPosition.name}.</li>
     *     <li>{@code maxPoint} is mapped from {@code userPosition.maxPoint}.</li>
     *     <li>{@code pointValue} is mapped from {@code userPosition.pointValue}.</li>
     *     <li>{@code departmentName} is mapped from {@code department.nameEleks}.</li>
     *     <li>{@code staffListId} is mapped from {@code staffListRecord.staffListId}.</li>
     * </ul>
     * </p>
     *
     * @param entity The {@link Result} entity to convert.
     * @return A {@link ResultDTO} representing the entity.
     */
    @Override
    @Mapping(target = "username", source = "user.name")
    @Mapping(target = "userPositionName", source = "userPosition.name")
    @Mapping(target = "maxPoint", source = "userPosition.maxPoint")
    @Mapping(target = "pointValue", source = "userPosition.pointValue")
    @Mapping(target = "departmentName", source = "department.nameEleks")
    @Mapping(target = "staffListId", source = "staffListRecord.staffListId")
    public abstract ResultDTO toDto(Result entity);

    /**
     * Converts a {@link Result} object into a list of string values for exporting to Excel or similar formats.
     *
     * @param result The {@link Result} object to convert.
     * @return A list of strings representing the fields of the {@link Result} object.
     */
    public List<String> toStringList(Result result) {
        List<String> listOfResult = new ArrayList<>();

        listOfResult.add(result.getStaffListRecord().getStaffListId());
        listOfResult.add(result.getUser().getName());
        listOfResult.add(Objects.requireNonNullElse(result.getDepartment().getName(), "Unknown Department"));
        listOfResult.add(Objects.requireNonNullElse(result.getDepartment().getDepartmentTemplateId(), "N/A"));
        listOfResult.add(Objects.requireNonNullElse(result.getUserPosition().getName(), "Unknown Position"));
        listOfResult.add(String.format(Objects.requireNonNullElse(result.getEmploymentStartDate(), "01-01-2024").toString()));
        listOfResult.add(String.format("%.2f", result.getEmployment()));
        listOfResult.add(String.format("%.2f", result.getEmploymentPartStaffList().orElse(0f)));
        listOfResult.add(String.format("%.2f", result.getEmploymentUserPositionPart()));
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
        listOfResult.add(Objects.requireNonNullElse(result.getComment(), "").toString());
        listOfResult.add(String.format(Objects.requireNonNullElse(result.getDate(), "01-01-2024").toString()));

        return listOfResult;
    }

    /**
     * Returns the column names for export to Excel format.
     *
     * <p>Each entry in the returned list represents a column name corresponding to a field in {@link Result}.</p>
     *
     * @return A list of column names for Excel export.
     */
    public List<String> toStringListColNameForExcel() {
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

    /**
     * Returns the column names for SQL query generation.
     *
     * <p>Each entry in the returned list represents a column name corresponding to a field in the database.</p>
     *
     * @return A list of column names for SQL query generation.
     */
    public List<String> toStringListColNameForSQL() {
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
