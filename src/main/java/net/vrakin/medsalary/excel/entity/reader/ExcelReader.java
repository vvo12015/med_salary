package net.vrakin.medsalary.excel.entity.reader;

import java.io.File;
import java.util.List;

public interface ExcelReader<E, D> {
    List<E> readAllEntries(File file);
    List<D> readAllDto(File file);

    List<String> isValidateFile(File file);
}
