package net.vrakin.medsalary.excel;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface ExcelHelper {
    public static final String EMPTY_SING_EXCEL = "-";
    public static final String EMPTY_SING = "0";

    public static final String WORD_SEPARATOR = "&&";
    public static final String PACKAGE_NUMBER_SEPARATOR = ",";
    List<String> readExcel(File file, int startColNumber);

    List<String> readRowCountExcel(File file, int startColNumber, int count);
    LocalDateTime mapToDate(String excelStringDate);
}
