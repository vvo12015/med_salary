package net.vrakin.medsalary.excel.entity.reader;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.exception.CellNullPointerException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import java.nio.file.Files;


@Slf4j
@Component
@NoArgsConstructor
public class FastExcelHelperImpl implements ExcelHelper{

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
                        log.info("{} - {}", r.getCellCount(), row);
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

        LocalDate datePart;

        LocalTime timePart;

        try {
            double excelDate = Double.parseDouble(excelStringDate);

            int days = (int) excelDate;
            double fraction = excelDate - days;

            datePart = baseDate.plusDays(days - 2); // -2 to adjust for the incorrect leap year in Excel

            int totalSecondsInDay = (int) (fraction * 24 * 3600);
            timePart = LocalTime.ofSecondOfDay(totalSecondsInDay);
        }catch (NumberFormatException e){
            return null;
        }

        return LocalDateTime.of(datePart, timePart);
    }

    @Override
    public List<String> readRowCountExcel(File fileLocation, int startRowNumber, int count){
        List<String> stringList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(fileLocation); ReadableWorkbook wb = new ReadableWorkbook(file)) {
            Sheet sheet = wb.getFirstSheet();

            try (Stream<Row> rows = sheet.openStream()) {
                rows.forEach(r-> {
                    if ((r.getRowNum() >= startRowNumber) &&
                            (r.getRowNum() < startRowNumber+count)
                    ) {
                        StringBuilder row = new StringBuilder();

                        for (Cell cell : r) {
                            if (Objects.isNull(cell)) try {
                                throw new CellNullPointerException("Cell is null");
                            } catch (CellNullPointerException e) {
                                throw new RuntimeException(e);
                            }
                            row.append(Objects.requireNonNullElse(cell.getRawValue(), "")).append(WORD_SEPARATOR);
                        }

                        stringList.add(row.toString());
                    }
                });
            }
        }catch (Exception e){
            for (StackTraceElement stackTraceElement :
                    e.getStackTrace()) {
                log.error(stackTraceElement.toString());
            }
        }

        return stringList;
    }

    @Override
    public void writeExcel(File file, List<List<String>> listCells) throws IOException {


        try (OutputStream os = Files.newOutputStream(file.toPath());
             Workbook wb = new Workbook(os, "MyApplication", "1.0")) {
            Worksheet ws = wb.newWorksheet("Премія");
            ws.width(0, 25);
            ws.width(1, 15);

            List<String> colNames = listCells.stream().findFirst().orElseThrow(()->new ResourceNotFoundException("List from resultList", "first el", "first el"));

            ws.range(0, 0, 0, colNames.size() - 1).style()
                    .fontName("Calibri")
                    .fontSize(11)
                    .bold()
                    .fontColor("#000000")  // Чорний шрифт
                    .fillColor("#00FF00")  // Яскраво-зелений фон
                    .set();

            for (int i = 0; i < listCells.size(); i++) {
                List<String> row = listCells.get(i);
                log.info("row: {}", row.toString());
                for (int j = 0; j < row.size(); j++) {
                    ws.value(i, j, row.get(j));
                }
            }
        }
    }
}


