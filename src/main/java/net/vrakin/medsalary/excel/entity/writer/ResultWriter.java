package net.vrakin.medsalary.excel.entity.writer;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.excel.entity.reader.ExcelHelper;
import net.vrakin.medsalary.mapper.ResultMapper;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Сервіс для запису даних результатів у файли Excel та SQL-скрипти.
 *
 * @author Автор
 */
@Service
public class ResultWriter implements ExcelWriter<Result>, TextWriter<Result> {

    private final ResultMapper resultMapper;
    private final ExcelHelper excelHelper;
    private final StorageService storageService;

    /**
     * Конструктор для ініціалізації маппера, хелпера та сервісу збереження файлів.
     *
     * @param resultMapper   Маппер для перетворення об'єктів Result у списки рядків.
     * @param excelHelper    Хелпер для роботи з Excel-файлами.
     * @param storageService Сервіс для управління файлами.
     */
    public ResultWriter(ResultMapper resultMapper,
                        ExcelHelper excelHelper,
                        StorageService storageService) {
        this.resultMapper = resultMapper;
        this.excelHelper = excelHelper;
        this.storageService = storageService;
    }

    /**
     * Записує результати у Excel-файл.
     *
     * @param results Список результатів, які необхідно записати.
     * @throws IOException Якщо виникає помилка при роботі з файлом.
     */
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

    /**
     * Повертає назви колонок для запису у Excel-файл.
     *
     * @return Список назв колонок.
     */
    private List<String> getColumnNames() {
        return resultMapper.toStringListColNameForExcel();
    }

    /**
     * Генерує SQL-скрипт для вставки результатів у базу даних.
     *
     * @param resultList Список результатів для збереження у SQL.
     */
    public void writeAllToSQL(List<Result> resultList) {
        LocalDateTime dateTime = LocalDateTime.now();
        String filename = String.format("result_%s.%s", dateTime.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm")), "sql");

        Path file = storageService.load(filename);
        try (var buffer = new BufferedWriter(new FileWriter(file.toFile()))) {
            var sb = new StringBuilder();

            sb.append(String.format("USE [DoctorEleks v1]\n" +
                    "GO\n" +
                    "\n" +
                    "INSERT INTO [dbo].[Premium](\n"));

            resultMapper.toStringListColNameForSQL()
                    .forEach(name ->
                            sb.append(String.format("[%s]\n, ", name)));
            sb.append("date)\n");

            for (Result result : resultList) {
                int i = 0;
                sb.append(String.format(Locale.US,
                        "SELECT '%s', N'%s', N'%s', %s, N'%s', '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, " +
                                "%s, %s, %s, %s, %s, %s, %s, GETDATE()\n" +
                                "UNION ALL \n",
                        resultMapper.toStringList(result).get(i++),// StaffListId
                        ResultWriter.apostroff(resultMapper.toStringList(result).get(i++)),// UserShortName
                        ResultWriter.apostroff(resultMapper.toStringList(result).get(i++)),// DepartmentName
                        resultMapper.toStringList(result).get(i++),// DepartmentID
                        ResultWriter.apostroff(resultMapper.toStringList(result).get(i++)),// UserPosition
                        resultMapper.toStringList(result).get(i++),// EmploymentStartDate
                        resultMapper.toStringList(result).get(i++),// Employment
                        resultMapper.toStringList(result).get(i++),// EmploymentPart
                        resultMapper.toStringList(result).get(i++),// EmploymentPosition
                        resultMapper.toStringList(result).get(i++),// HourCoefficient
                        resultMapper.toStringList(result).get(i++),// NightHours
                        resultMapper.toStringList(result).get(i++),// Point
                        resultMapper.toStringList(result).get(i++),// PointValue
                        resultMapper.toStringList(result).get(i++),// BasicPremium
                        resultMapper.toStringList(result).get(i++),// HospPremium
                        resultMapper.toStringList(result).get(i++),// CountEMR_stationary
                        resultMapper.toStringList(result).get(i++),// AmblNSZU_Premium
                        resultMapper.toStringList(result).get(i++),// SumForAmlPackage
                        resultMapper.toStringList(result).get(i++),// CountEMR_priorityService
                        resultMapper.toStringList(result).get(i++),// OneDaySurgery
                        resultMapper.toStringList(result).get(i++),// CountEMR_oneDaySurgery
                        resultMapper.toStringList(result).get(i)// OtherPremium
                ));
            }

            buffer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Замінює апостроф у рядку для коректного SQL-запиту.
     *
     * @param name Вхідний рядок.
     * @return Рядок без некоректних апострофів.
     */
    public static String apostroff(String name) {
        return name.replace("'", "''").replace("’", "''");
    }
}
