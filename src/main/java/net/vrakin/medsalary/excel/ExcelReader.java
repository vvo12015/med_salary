package net.vrakin.medsalary.excel;

import java.io.File;
import java.util.List;

public interface ExcelReader<E, D> {
    List<E> readAllEntries(File file);
    List<D> readAllDto(File file);

    boolean isValidateFile(File file);

    D toDTOFromString(String s);
}
