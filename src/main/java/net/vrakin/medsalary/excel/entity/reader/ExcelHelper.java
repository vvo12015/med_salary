package net.vrakin.medsalary.excel.entity.reader;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExcelHelper {
    String EMPTY_SING_EXCEL = "-";
    String EMPTY_SING = "0";

    String WORD_SEPARATOR = "&&";
    String PACKAGE_NUMBER_SEPARATOR = ",";
    int FIRST_ROW_NUMBER = 1;
    String FILE_EXTENSION = ".xlsx";
    List<String> readExcel(File file, int startColNumber);

    List<String> readRowCountExcel(File file, int startRowNumber, int count) throws Exception;
    LocalDate mapToDate(String excelStringDate);
    LocalDateTime mapToDateTime(String excelStringDate);

    void writeExcel(File file, List<List<String>> listCells) throws IOException;
}
