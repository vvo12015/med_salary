package net.vrakin.medsalary.excel.entity.reader;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.*;
import net.vrakin.medsalary.mapper.StaffListRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@Slf4j
public class StaffListRecordExcelReader extends AbstractExcelReader<StaffListRecord, StaffListRecordDTO>
        implements ExcelReader<StaffListRecord, StaffListRecordDTO>
{

    public static final int STAFF_LIST_ID_INDEX = 1;
    public static final int PIB_INDEX = 2;
    private static final Integer DEPARTMENT_NAME_INDEX = 3;
    public static final int DEPARTMENT_CODE_INDEX = 4;
    public static final int USER_POSITION_NAME_INDEX = 5;
    public static final int USER_POSITION_CODE_INDEX = 6;
    private static final int EMPLOYMENT_START_DATE_INDEX = 7;
    private static final int EMPLOYMENT_END_DATE_INDEX = 8;
    private static final int SALARY_INDEX = 11;
    public static final int EMPLOYMENT_INDEX = 21;
    public static final int IPN_INDEX = 22;
    public static final int CATEGORY_INDEX = 23;
    public static final int START_STAFFLIST_ROW_NUMBER = 5;

    @Autowired
    public StaffListRecordExcelReader(ExcelHelper excelHelper, StaffListRecordMapper mapper) {
        super(excelHelper, mapper);
        generateFormatDetails();
    }

    @Override
    protected List<String> filterRow(File file) {
        List<String> stringList = excelHelper.readExcel(file, fileFormatDetails.getStartColNumber());

        return stringList.stream().filter(s-> {
            String str = s.split(ExcelHelper.WORD_SEPARATOR)[0];
            log.info("filretRow: \"{}\", !str.matches(): {}", str, str.matches("\\d+"));

            return (str.matches("\\d+"));
        }).collect(Collectors.toList());
    }

    public StaffListRecordDTO toDTOFromString(String stringDTO, LocalDate period) {

        List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR))
                .collect(Collectors.toList());

        StaffListRecordDTO dto = new StaffListRecordDTO();

        if ((stringList.size()>STAFF_LIST_ID_INDEX) &&
                Objects.nonNull(stringList.get(STAFF_LIST_ID_INDEX))) dto.setStaffListId(stringList.get(STAFF_LIST_ID_INDEX));

        UserPositionDTO userPositionDTO = getUserPositionDTO(stringList);
        dto.setUserPosition(userPositionDTO);

        DepartmentDTO departmentDTO = new DepartmentDTO();

        if ((stringList.size()> DEPARTMENT_CODE_INDEX) &&
                Objects.nonNull(stringList.get(DEPARTMENT_CODE_INDEX))){
            departmentDTO.setDepartmentIsProId(stringList.get(DEPARTMENT_CODE_INDEX));
            departmentDTO.setName(stringList.get(DEPARTMENT_NAME_INDEX));
        }
        dto.setDepartment(departmentDTO);

        if ((stringList.size()>EMPLOYMENT_INDEX) &&
                Objects.nonNull(stringList.get(EMPLOYMENT_INDEX)) &&
                    !stringList.get(EMPLOYMENT_INDEX).equals("null"))
            dto.setEmployment(Float.parseFloat(stringList.get(EMPLOYMENT_INDEX)));

        UserDTO userDTO = new UserDTO();
        if ((stringList.size()>IPN_INDEX) &&
                Objects.nonNull(stringList.get(IPN_INDEX))){
            userDTO.setIpn(stringList.get(IPN_INDEX));
        }

        if (Objects.nonNull(stringList.get(STAFF_LIST_ID_INDEX))){
            userDTO.setName(stringList.get(PIB_INDEX));
        }

        if (Objects.nonNull(stringList.get(EMPLOYMENT_START_DATE_INDEX))){
            dto.setEmploymentStartDate(excelHelper.mapToDate(stringList.get(EMPLOYMENT_START_DATE_INDEX)));
        }

        if (Objects.nonNull(stringList.get(EMPLOYMENT_END_DATE_INDEX))){
            dto.setEmploymentEndDate(excelHelper.mapToDate(stringList.get(EMPLOYMENT_END_DATE_INDEX)));
        }

        if (Objects.nonNull(stringList.get(SALARY_INDEX))){
            dto.setSalary(Float.parseFloat(stringList.get(SALARY_INDEX)));
        }

        dto.setStartDate(period.atTime(0, 0));

        if (Objects.nonNull(stringList.get(CATEGORY_INDEX))){
            dto.setPremiumCategoryName(stringList.get(CATEGORY_INDEX));
        }
        dto.setStatus(DTOStatus.FROM_FILE);

        dto.setUser(userDTO);

        return dto;
    }

    private static UserPositionDTO getUserPositionDTO(List<String> stringList) {
        UserPositionDTO userPositionDTO = new UserPositionDTO();
        if ((stringList.size()> USER_POSITION_CODE_INDEX) &&
                Objects.nonNull(stringList.get(USER_POSITION_CODE_INDEX))){
            userPositionDTO.setCodeIsPro(stringList.get(USER_POSITION_CODE_INDEX));
        }

        if ((stringList.size()> USER_POSITION_NAME_INDEX) &&
                Objects.nonNull(stringList.get(USER_POSITION_NAME_INDEX))){

            userPositionDTO.setName(stringList.get(USER_POSITION_NAME_INDEX));

        }
        return userPositionDTO;
    }

    @Override
    protected void generateFormatDetails() {

        List<Column> columns = new ArrayList<>();

        columns.add(new Column("Табельний №", STAFF_LIST_ID_INDEX));
        columns.add(new Column("Прізвище Ім'я По-батькові", PIB_INDEX));
        columns.add(new Column("Підрозділ", DEPARTMENT_NAME_INDEX));
        columns.add(new Column("Підрозділ (код)", DEPARTMENT_CODE_INDEX));
        columns.add(new Column("Посада", USER_POSITION_NAME_INDEX));
        columns.add(new Column("Посада (код)", USER_POSITION_CODE_INDEX));
        columns.add(new Column("Дата прийому", EMPLOYMENT_START_DATE_INDEX));
        columns.add(new Column("Дата звільнення", EMPLOYMENT_END_DATE_INDEX));
        columns.add(new Column("Оклад/тариф", SALARY_INDEX));
        columns.add(new Column("Кількість ставок", EMPLOYMENT_INDEX));
        columns.add(new Column("РНОКПП (ІПН)", IPN_INDEX));
        columns.add(new Column("Категорія", CATEGORY_INDEX));

        this.fileFormatDetails = new FileFormatDetails(columns, 24, START_STAFFLIST_ROW_NUMBER);
    }
}
