package net.vrakin.medsalary.excel.entity.reader;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.TimeSheet;
import net.vrakin.medsalary.dto.TimeSheetDTO;
import net.vrakin.medsalary.mapper.TimeSheetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@NoArgsConstructor
@Slf4j
public class TimeSheetExcelReader extends AbstractExcelReader<TimeSheet, TimeSheetDTO>
        implements ExcelReader<TimeSheet, TimeSheetDTO>{

    public static final int TIME_SHEET_STAFFLISTID_INDEX = 1;
    public static final int TIME_SHEET_NAME_INDEX = 2;
    public static final int TIME_SHEET_PLAN_INDEX = 4;
    public static final int TIME_SHEET_FACT_INDEX = 5;

    @Autowired
        public TimeSheetExcelReader(ExcelHelper excelHelper, TimeSheetMapper mapper) {
            super(excelHelper, mapper);

            generateFormatDetails();
        }

        @Override
        protected List<String> filterRow(File file) {
            return excelHelper.readExcel(file, fileFormatDetails.getStartColNumber());
        }

    @Override
    protected void generateFormatDetails() {

        List<Column> columns = new ArrayList<>();

        columns.add(new Column("Таб.№", TIME_SHEET_STAFFLISTID_INDEX));
        columns.add(new Column("Прізвище Ім'я По-батькові", TIME_SHEET_NAME_INDEX));
        columns.add(new Column("Пл.|Годин.", TIME_SHEET_PLAN_INDEX));
        columns.add(new Column("Фк.|Годин.", TIME_SHEET_FACT_INDEX));

        this.fileFormatDetails = new FileFormatDetails(columns, columns.size(), ExcelHelper.FIRST_ROW_NUMBER);
    }

        @Override
        public TimeSheetDTO toDTOFromString(String stringDTO) {

            log.info("Reading timeSheet: {}", stringDTO);
            List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR))
                    .toList();
            TimeSheetDTO dto = new TimeSheetDTO();

            if (Objects.isNull(stringList.getFirst()) &&
                    stringList.size() >= fileFormatDetails.getFileColumnCount()){

                dto.setStaffListRecordId(stringList.get(TIME_SHEET_STAFFLISTID_INDEX));
                dto.setFactTime(Float.parseFloat(stringList.get(TIME_SHEET_FACT_INDEX)));
                dto.setFactTime(Float.parseFloat(stringList.get(TIME_SHEET_PLAN_INDEX)));
            }

            return dto;
        }
    }

