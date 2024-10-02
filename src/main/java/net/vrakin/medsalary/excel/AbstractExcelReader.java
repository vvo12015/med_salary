package net.vrakin.medsalary.excel;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@NoArgsConstructor
@Slf4j
public abstract class AbstractExcelReader<E, D> implements ExcelReader<E, D> {

    protected BaseMapper<E, D> mapper;

    protected ExcelHelper excelHelper;

    protected final List<Column> columns = new ArrayList<>();

    protected String className;

    protected int startColNumber;

    @Autowired
    public AbstractExcelReader(ExcelHelper excelHelper,
                                    BaseMapper<E, D> mapper) {
        this.excelHelper = excelHelper;
        this.mapper = mapper;
        createColumnList();
    }

    protected abstract List<String> filterRow(File file);

    public List<E> readAllEntries(File file){
        return mapper.toEntityList(readAllDto(file));
    }

    public List<D> readAllDto(File file){
        return filterRow(file).stream().map(this::toDTOFromString)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValidateFile(File file) {

        Optional<String> tableHeadFromFileOptional = excelHelper.readRowCountExcel(file, startColNumber, 1).stream().findFirst();
        String tableHeadFromFile;

        if (tableHeadFromFileOptional.isPresent()){
            tableHeadFromFile = tableHeadFromFileOptional.get();
        }else {
            log.warn("file: {}, startColNumber: {}, countRows: {}", file.getAbsolutePath(), startColNumber,
                    (long) excelHelper.readRowCountExcel(file, startColNumber, 1).size());
            return false;
        }

        List<String> tableHeads = getCells(tableHeadFromFile);

        for(Column domainColumn: columns){
            log.info("tableHeads.get(domainColumn.getIndexColumn()): {}, domainColumn.getNameColumn(): {}",
                    Objects.requireNonNullElse(tableHeads.get(domainColumn.getIndexColumn()), "null")
                    , Objects.requireNonNullElse(domainColumn.getNameColumn(), "null")
            );
            if (!tableHeads.get(domainColumn.getIndexColumn()).equals(domainColumn.getNameColumn())){
                return false;
            }
        }

        return true;
    }

    protected List<String> getCells(String row){
        return Arrays.stream(row.split(ExcelHelper.WORD_SEPARATOR))
                .collect(Collectors.toList());
    }

    protected abstract void createColumnList();

   
}
