package net.vrakin.medsalary.excel;

import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.dto.ServicePackageDTO;
import net.vrakin.medsalary.mapper.ServicePackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicePackageExcelReader extends AbstractExcelReader<ServicePackage, ServicePackageDTO>
    implements ExcelReader<ServicePackage, ServicePackageDTO>{
    public static final int SERVICE_PACKAGE_NAME_INDEX = 0;
    public static final int SERVICE_PACKAGE_NUMBER = 1;
    public static final int SERVICE_IS_HOSP = 2;
    public static final int SERVICE_PACKAGE_IS_OPERATION = 3;
    public static final int SERVICE_PACKAGE_IS_CALCULATE = 5;

    @Autowired
    public ServicePackageExcelReader(ExcelHelper excelHelper, ServicePackageMapper mapper) {
        super(excelHelper, mapper);
        this.startColNumber = 1;

        createColumnList();
    }

    @Override
    protected List<String> filterRow(File file) {
        return excelHelper.readExcel(file, startColNumber);
    }

    @Override
    protected void createColumnList() {
        columns.clear();
        columns.add(new Column("Назва", SERVICE_PACKAGE_NAME_INDEX));
        columns.add(new Column("Номер", SERVICE_PACKAGE_NUMBER));
        columns.add(new Column("Стаціонарний/Амбулаторний", SERVICE_IS_HOSP));
        columns.add(new Column("Операції", SERVICE_PACKAGE_IS_OPERATION));
        columns.add(new Column("Наш підрахунок", SERVICE_PACKAGE_IS_CALCULATE));
    }

    @Override
    public ServicePackageDTO toDTOFromString(String stringDTO) {

        List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR))
                .collect(Collectors.toList());
        ServicePackageDTO dto = new ServicePackageDTO();
        if (stringList.size() > 5){

            dto.setName(stringList.get(SERVICE_PACKAGE_NAME_INDEX));
            dto.setNumber(stringList.get(SERVICE_PACKAGE_NUMBER));
            dto.setHospKind(stringList.get(SERVICE_IS_HOSP)
                    .equals("Амбулаторний")?ServicePackage.HospKind.AMBULATORY
                            :stringList.get(SERVICE_IS_HOSP).equals("Стаціонарний")? ServicePackage.HospKind.STATIONARY:ServicePackage.HospKind.MIXED
                    );
            dto.setOperationKind(stringList.get(SERVICE_PACKAGE_IS_OPERATION)
                    .equals("Так")?ServicePackage.OperationKind.OPERATION
                    :stringList.get(SERVICE_PACKAGE_IS_OPERATION).equals("Ні")? ServicePackage.OperationKind.NO_OPERATION:ServicePackage.OperationKind.MIXED
            );
            dto.setIsCalculate(stringList.get(SERVICE_PACKAGE_IS_CALCULATE).equals("0"));
        }


        return dto;
    }
}
