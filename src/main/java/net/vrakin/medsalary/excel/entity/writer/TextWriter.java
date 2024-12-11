package net.vrakin.medsalary.excel.entity.writer;

import java.util.List;

public interface TextWriter<E>{
    void writeAllToSQL(List<E> list);
}
