package net.vrakin.medsalary.excel;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
@Slf4j
@Component
@NoArgsConstructor
public class FastExcelHelper implements ExcelHelper{

    public List<String> readExcel(File fileLocation, int startColNumber){
        List<String> stringList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(fileLocation); ReadableWorkbook wb = new ReadableWorkbook(file)) {
            Sheet sheet = wb.getFirstSheet();

            try (Stream<Row> rows = sheet.openStream()) {
                rows.forEach(r -> {

                    if ((r.getRowNum() > startColNumber) && r.getCellCount()>0
                    ) {
                        StringBuilder row = new StringBuilder();

                        for (Cell cell : r) {
                            if (Objects.nonNull(cell)) row.append(cell.getRawValue()).append(WORD_SEPARATOR);
                            else row.append(" ").append(WORD_SEPARATOR);
                        }
                        log.info("{} - {}", r.getCellCount(), row.toString());
                        stringList.add(row.toString());
                    }
                });
            }
        }catch (IOException e){
            for (StackTraceElement stackTraceElement :
                    e.getStackTrace()) {
                log.error(stackTraceElement.toString());
            }
        }

        return stringList;
    }

    @Override
    public LocalDateTime mapToDate(String excelStringDate){

        LocalDate baseDate = LocalDate.of(1900, 1, 1);

        double excelDate = Double.valueOf(excelStringDate);

        int days = (int) excelDate;
        double fraction = excelDate - days;

        LocalDate datePart = baseDate.plusDays(days - 2); // -2 to adjust for the incorrect leap year in Excel

        int totalSecondsInDay = (int) (fraction * 24 * 3600);
        LocalTime timePart = LocalTime.ofSecondOfDay(totalSecondsInDay);

        return LocalDateTime.of(datePart, timePart);
    }

    @Override
    public List<String> readRowCountExcel(File fileLocation, int startColNumber, int count) {
        List<String> stringList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(fileLocation); ReadableWorkbook wb = new ReadableWorkbook(file)) {
            Sheet sheet = wb.getFirstSheet();

            try (Stream<Row> rows = sheet.openStream()) {
                rows.forEach(r-> {
                    if ((r.getRowNum() >= startColNumber-1) &&
                            (r.getRowNum() < startColNumber+count)
                    ) {
                        StringBuilder row = new StringBuilder();

                        for (Cell cell : r) {
                            row.append(Objects.requireNonNullElse(cell.getRawValue(), "")).append(WORD_SEPARATOR);
                        }

                        stringList.add(row.toString());
                    }
                });
            }
        }catch (IOException e){
            for (StackTraceElement stackTraceElement :
                    e.getStackTrace()) {
                log.error(stackTraceElement.toString());
            }
        }

        return stringList;
    }
}


