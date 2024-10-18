package net.vrakin.medsalary.excel.entity.reader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileFormatDetails {

    private List<Column> columns = new ArrayList<>();

    private int fileColumnCount;

    private int startColNumber;
}
