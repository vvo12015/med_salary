package net.vrakin.medsalary.excel;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.dto.DepartmentDTO;
import net.vrakin.medsalary.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class DepartmentExcelReader extends AbstractExcelReader<Department, DepartmentDTO>
        implements ExcelReader<Department, DepartmentDTO>{

    public static final int DEPARTMENT_TEMPLATE_ID_INDEX = 0;
    public static final int DEPARTMENT_IS_PRO_ID_INDEX = 1;
    public static final int DEPARTMENT_ELEKS_NAME_INDEX = 2;
    public static final int DEPARTMENT_ISPRO_NAME_INDEX = 3;

    public static final int DEPARTMENT_SERVICE_PACKAGE = 4;

    @Autowired
        public DepartmentExcelReader(ExcelHelper excelHelper, DepartmentMapper mapper) {
            super(excelHelper, mapper);
            this.startColNumber = 1;

            columns.clear();
            createColumnList();
        }

        @Override
        protected List<String> filterRow(File file) {
            return excelHelper.readExcel(file, startColNumber);
        }

    @Override
    protected void createColumnList() {
        columns.clear();
        columns.add(new Column("departmentEleksId", DEPARTMENT_TEMPLATE_ID_INDEX));
        columns.add(new Column("departmentIsProId", DEPARTMENT_IS_PRO_ID_INDEX));
        columns.add(new Column("departmentEleksName", DEPARTMENT_ELEKS_NAME_INDEX));
        columns.add(new Column("departmentIsProName", DEPARTMENT_ISPRO_NAME_INDEX));
        columns.add(new Column("Можливі пакети", DEPARTMENT_SERVICE_PACKAGE));
    }

        @Override
        public DepartmentDTO toDTOFromString(String stringDTO) {

            List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR))
                    .collect(Collectors.toList());

            DepartmentDTO dto = new DepartmentDTO();

            if (stringList.size()<=columns.size() &&
                    !stringList.get(0).isEmpty()) {
                dto.setDepartmentTemplateId(stringList.get(DEPARTMENT_TEMPLATE_ID_INDEX));
                dto.setDepartmentIsProId(stringList.get(DEPARTMENT_IS_PRO_ID_INDEX));
                dto.setNameEleks(stringList.get(DEPARTMENT_ELEKS_NAME_INDEX));
                dto.setName(stringList.get(DEPARTMENT_ISPRO_NAME_INDEX));
                dto.setServicePackages(stringList.get(DEPARTMENT_SERVICE_PACKAGE));
            }

            return dto;
        }
    }

