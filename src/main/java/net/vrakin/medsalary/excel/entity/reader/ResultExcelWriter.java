package net.vrakin.medsalary.excel.entity.reader;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.mapper.ResultMapper;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResultExcelWriter implements ExcelWriter<Result> {

    private final ResultMapper resultMapper;

    private final ExcelHelper excelHelper;

    private final StorageService storageService;

    public ResultExcelWriter(ResultMapper resultMapper,
                             ExcelHelper excelHelper,
                             StorageService storageService) {
        this.resultMapper = resultMapper;
        this.excelHelper = excelHelper;
        this.storageService = storageService;
    }

    @Override
    public void writeAll(List<Result> results) throws IOException {

        List<List<String>> rows = new ArrayList<>();
        rows.add(getColumnNames());

        rows.addAll(results.stream()
                .map(resultMapper::toStringList)
                .toList());

        LocalDateTime dateTime = LocalDateTime.now();
        String filename = String.format("result_%s.%s", dateTime.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm")), "xlsx");

        Path file = storageService.load(filename);
        excelHelper.writeExcel(file.toFile(), rows);
    }

    private List<String> getColumnNames() {

        List<String> resultColumnNames = new ArrayList<>();

        resultColumnNames.add("Id");
        resultColumnNames.add("ПІБ працівника");
        resultColumnNames.add("Назва відділення");
        resultColumnNames.add("Посада");
        resultColumnNames.add("Ставка");
        resultColumnNames.add("Премія стаціонар");
        resultColumnNames.add("Премія поліклініка");
        resultColumnNames.add("Премія хір. одного дня");
        resultColumnNames.add("Дата генерації запису");

        return resultColumnNames;
    }
}
