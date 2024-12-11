package net.vrakin.medsalary.excel.entity.writer;

import java.io.IOException;
import java.util.List;

public interface ExcelWriter<E> {
    void writeAll(List<E> entities) throws IOException;
}
