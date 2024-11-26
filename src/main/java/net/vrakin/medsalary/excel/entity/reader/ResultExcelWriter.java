package net.vrakin.medsalary.excel.entity.reader;

import net.vrakin.medsalary.domain.Result;
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

    private List<String> getColumnNames(){

        return resultMapper.toStringListColNames();
    }

    public void writeAllTxt(List<Result> resultList) {
        LocalDateTime dateTime = LocalDateTime.now();
        String filename = String.format("result_%s.%s", dateTime.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm")), "sql");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Path file = storageService.load(filename);
        try(var buffer = new BufferedWriter(new FileWriter(file.toFile()))){
            var sb = new StringBuilder();

            sb.append("USE [DoctorEleks v1]\n" +
                    "GO\n" +
                    "\n" +
                    "INSERT INTO [dbo].[Premium]\n" +
                    "           ([UserShortName]\n" +
                    "           ,[UserPosition]\n" +
                    "           ,[Point]\n" +
                    "           ,[PointValue]\n" +
                    "           ,[Employment]\n" +
                    "           ,[BasicPremium]\n" +
                    "           ,[DepartmentID]\n" +
                    "           ,[PremiumCreationDate]\n" +
                    "           ,[DepartmentName]\n" +
                    "           ,[PremiumStartDate]\n" +
                    "           ,[PremiumEndDate]\n" +
                    "           ,[sl_id]\n" +
                    "           ,[HourCoefficient]\n" +
                    "           ,[EmploymentPart]\n" +
                    "           ,[EmploymentPosition]\n" +
                    "           ,[HospNSZU_Premium]\n" +
                    "           ,[CountEMR_stationary]\n" +
                    "           ,[CountEMR_ambulatory]\n" +
                    "           ,[CountEMR_oneDaySurgery]\n" +
                    "           ,[CountEMR_priorityService]\n" +
                    "           ,[SumForAmlPackage]\n" +
                    "           ,[AmblNSZU_Premium]\n" +
                    "           ,[OneDaySurgery]\n" +
                    "           ,[OtherPremium])\n" +
                    "           ,[EmploymentStartDate])\n");

            for (Result result :
                    resultList) {

                sb.append(String.format(Locale.US,
                        "SELECT N'%s', N'%s', %d, %d, %.2f, %.2f, %s, GETDATE(), N'%s', GETDATE(), NULL, %s, %.2f, %.2f, %.2f, " +
                                "%.2f, %d, %d, %d, %d, %.2f, %.2f, %f, %.2f, '%s' " +
                                "\nUNION ALL \n",
                        ResultExcelWriter.apostroff(result.getUser().getName()),//UserShortName
                        ResultExcelWriter.apostroff(result.getUserPosition().getName()),//UserPosition
                        result.getUserPosition().getMaxPoint(),//Point
                        result.getUserPosition().getPointValue(),//PointValue
                        result.getEmployment(),//Employment
                        result.getHospNSZU_Premium()+ result.getAmblNSZU_Premium()+result.getOtherPremium()+ result.getOneDaySurgery(),//BasicPremium
                        result.getDepartment().getDepartmentTemplateId(),//DepartmentTemplate
                        ResultExcelWriter.apostroff(result.getDepartment().getNameEleks()),//DepartmentName
                        result.getStaffListRecord().getStaffListId(),//StaffListId
                        result.getHourCoefficient(),//HourCoefficient
                        result.getEmploymentPart(),//EmploymentPart
                        (result.getEmploymentPart() * result.getEmployment()),//EmploymentPartPosition
                        result.getHospNSZU_Premium(),//HospPremium
                        result.getCountEMR_stationary(),//CountEMR_stationary
                        result.getCountEMR_ambulatory(),//CountEMR_ambulatory
                        result.getCountEMR_oneDaySurgery(),//CountEMR_oneDaySurgery
                        result.getCountEMR_priorityService(),//CountEMR_priorityService
                        result.getSumForAmlPackage(),//SumForAmlPackage
                        result.getAmblNSZU_Premium(),//AmblNSZU_Premium
                        result.getOneDaySurgery(),//OneDaySurgery
                        result.getOtherPremium(),//OtherPremium
                        result.getEmploymentStartDate().format(formatter)//EmploymentStartDate
                ));

            }

            buffer.write(sb.toString());
        }catch (IOException e){

        }
    }

    public static String apostroff(String name) {
        return name.replace("'", "''").replace("’", "''");
    }
}
