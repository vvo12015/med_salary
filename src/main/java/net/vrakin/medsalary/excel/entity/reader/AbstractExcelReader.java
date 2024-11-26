package net.vrakin.medsalary.excel.entity.reader;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.mapper.BaseMapper;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@NoArgsConstructor
@Slf4j
public abstract class AbstractExcelReader<E, D> implements ExcelReader<E, D> {
    private static final int ONE_COLUMN_NUMBER = 1;
    public static final String MESSAGE_ABOUT_NOT_MATCH_COUNT_COLUMNS_ENG = "The number of columns in the file does not match: %s";
    public static final String MESSAGE_ABOUT_NOT_MATCH_COUNT_COLUMNS_UA = "У файлі не збігається кількість колонок в файлі/в базі: %d/%d %s";

    protected BaseMapper<E, D> mapper;
    protected ExcelHelper excelHelper;
    protected FileFormatDetails fileFormatDetails;

    public AbstractExcelReader(ExcelHelper excelHelper,
                                    BaseMapper<E, D> mapper) {
        this.excelHelper = excelHelper;
        this.mapper = mapper;
        generateFormatDetails();
    }

    protected abstract List<String> filterRow(File file);

    public List<E> readAllEntries(File file, LocalDate period){
        var dtos =  readAllDto(file, period);
        return mapper.toEntityList(dtos);
    }

    public List<D> readAllDto(File file, LocalDate period){
        return filterRow(file).stream().map(s ->toDTOFromString(s, period))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> isValidateFile(File file){

        List<String> errors = new ArrayList<>();

        Optional<String> tableHeadFromFileOptional = Optional.empty();

        try {
            tableHeadFromFileOptional = excelHelper
                    .readRowCountExcel(file, fileFormatDetails.getStartColNumber(), ONE_COLUMN_NUMBER).stream().findFirst();
        } catch (Exception e) {
            String errorText = "Помилка читання файлу";
            errors.add(errorText);
            log.info("Read file exception");
        }
        String tableHeadFromFile;

        if (Objects.requireNonNull(tableHeadFromFileOptional).isPresent()){
            tableHeadFromFile = tableHeadFromFileOptional.get();
        }else {
            String errorText = String.format("""
                                Error in reading the line of columns name\s
                                file: %s, startColNumber: %d, actualCountRows: %d
                                """, file.getAbsolutePath(), fileFormatDetails.getStartColNumber(),
                                fileFormatDetails.getFileColumnCount());
            log.info(errorText);
            errors.add("Неприпустимий формат файлу");
            return errors;
        }

        List<String> tableHeads = getCells(tableHeadFromFile);

        if (tableHeads.size() != fileFormatDetails.getFileColumnCount()){
            String errorTextLog = String.format(MESSAGE_ABOUT_NOT_MATCH_COUNT_COLUMNS_ENG, tableHeadFromFile);
            String errorText = String.format(MESSAGE_ABOUT_NOT_MATCH_COUNT_COLUMNS_UA,
                    tableHeads.size(), fileFormatDetails.getFileColumnCount(), tableHeads);
            errors.add(errorText);
            log.info(errorTextLog);
            return errors;
        }

        for(Column domainColumn: fileFormatDetails.getColumns()){

            if (domainColumn.getIndexColumn()<tableHeads.size()) {
                log.info("{}: tableHeads.get(domainColumn.getIndexColumn()): {}, domainColumn.getNameColumn(): {}",
                        this.getClass().getName(),
                        Objects.requireNonNullElse(tableHeads.get(domainColumn.getIndexColumn()), "null")
                                                                    , domainColumn.getNameColumn());
                if (!tableHeads.get(domainColumn.getIndexColumn()).equals(domainColumn.getNameColumn())) {
                    String errorLog = String.format("Column name: %s, actual: %s", domainColumn.getNameColumn(), tableHeads.get(domainColumn.getIndexColumn()));
                    String errorText = String.format("Назви колонок не співпадають колонка бази: %s, у файлі: %s",
                            domainColumn.getNameColumn(), tableHeads.get(domainColumn.getIndexColumn()));
                    log.info(errorLog);
                    errors.add(errorText);
                }
            }else{
                String errorLog = String.format("Column index from DB(%d) rather tableHeads.size(%d)", domainColumn.getIndexColumn(), tableHeads.size());
                String errorText = String.format("Індех колонки в DB (%d) більший ніж tableHeads.size(%d)", domainColumn.getIndexColumn(), tableHeads.size());
                log.info(errorLog);
                errors.add(errorText);
            }
        }

        return errors;
    }

    protected List<String> getCells(String row){
        return Arrays.stream(row.split(ExcelHelper.WORD_SEPARATOR))
                .collect(Collectors.toList());
    }

    protected abstract void generateFormatDetails();

    protected abstract D toDTOFromString(String s, LocalDate period);
}
