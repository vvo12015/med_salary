package net.vrakin.medsalary.excel;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.UserPositionDTO;
import net.vrakin.medsalary.mapper.UserPositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class UserPositionExcelReader extends AbstractExcelReader<UserPosition, UserPositionDTO>
        implements ExcelReader<UserPosition, UserPositionDTO>{

    public static final int USER_POSITION_NAME_INDEX = 0;
    public static final int USER_POSITION_CODE_IS_PRO_INDEX = 1;
    public static final int USER_POSITION_MAX_POINT = 2;
    public static final int USER_POSITION_POINT_VALUE = 3;
    public static final int USER_POSITION_BASIC_PREMIUM = 4;
    public static final int USER_POSITION_SERVICE_PACKAGE = 5;
    private static final int USER_POSITION_NSZU_NAME = 6;

    @Autowired
        public UserPositionExcelReader(ExcelHelper excelHelper, UserPositionMapper mapper) {
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
        columns.add(new Column("Посада", USER_POSITION_NAME_INDEX));
        columns.add(new Column("Посада (код)", USER_POSITION_CODE_IS_PRO_INDEX));
        columns.add(new Column("максимальна сума балів", USER_POSITION_MAX_POINT));
        columns.add(new Column("сума за 1 бал", USER_POSITION_POINT_VALUE));
        columns.add(new Column("базова премія", USER_POSITION_BASIC_PREMIUM));
        columns.add(new Column("номера пакетів", USER_POSITION_SERVICE_PACKAGE));
        columns.add(new Column("посади в розшифровці", USER_POSITION_NSZU_NAME));
    }

        @Override
        public UserPositionDTO toDTOFromString(String stringDTO) {

            List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR))
                    .collect(Collectors.toList());
            UserPositionDTO dto = new UserPositionDTO();

            if (stringList.size() >= columns.size()){

                dto.setName(stringList.get(USER_POSITION_NAME_INDEX));
                dto.setCodeIsPro(stringList.get(USER_POSITION_CODE_IS_PRO_INDEX));
                dto.setMaxPoint(Integer.parseInt(stringList.get(USER_POSITION_MAX_POINT)));
                dto.setPointValue(Integer.parseInt(stringList.get(USER_POSITION_POINT_VALUE)));
                dto.setBasicPremium(Integer.parseInt(stringList.get(USER_POSITION_BASIC_PREMIUM)));
                dto.setServicePackageNumbers(stringList.get(USER_POSITION_SERVICE_PACKAGE));
                dto.setNszuName(stringList.get(USER_POSITION_NSZU_NAME));
            }


            return dto;
        }
    }

