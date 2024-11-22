package net.vrakin.medsalary.excel.entity.reader;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public interface ExcelReader<E, D> {
    List<E> readAllEntries(File file, LocalDate period);
    List<D> readAllDto(File file, LocalDate period);

    List<String> isValidateFile(File file);
}
