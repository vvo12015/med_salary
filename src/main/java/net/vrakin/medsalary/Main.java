package net.vrakin.medsalary;

import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.excel.entity.writer.ResultWriter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        System.out.println(passwordEncoder.encode("!!!&&&dpemcyd"));

        Result result = new Result();

        result.setComment("comment");
        User user = new User();
        user.setName("userName");
        result.setUser(user);
        UserPosition userPosition = new UserPosition();
        userPosition.setName("userPosition'Name");
        userPosition.setMaxPoint(8);
        userPosition.setPointValue(200);
        result.setUserPosition(userPosition);
        result.setEmployment(1.00f);
        result.setEmploymentPart(1.00f);
        result.setHospNSZU_Premium(1000.0f);
        result.setAmblNSZU_Premium(2000.0f);
        Department department = new Department();
        department.setDepartmentIsProId("001");
        department.setDepartmentTemplateId("002");
        department.setNameEleks("DoctorEleksName");
        result.setDepartment(department);
        StaffListRecord staffListRecord = new StaffListRecord();
        staffListRecord.setStaffListId("003");
        result.setStaffListRecord(staffListRecord);
        result.setHourCoefficient(1.0f);
        result.setCountEMR_stationary(1);//CountEMR_stationary
        result.setCountEMR_ambulatory(2);//CountEMR_ambulatory
        result.setCountEMR_oneDaySurgery(3);//CountEMR_oneDaySurgery
        result.setCountEMR_priorityService(4);//CountEMR_priorityService
        result.setSumForAmlPackage(500.0f);//SumForAmlPackage
        result.setAmblNSZU_Premium(1000.f);//AmblNSZU_Premium
        result.setOneDaySurgeryPremium(8000.f);//OneDaySurgery
        result.setOtherPremium(10000.0f);//OtherPremium
        result.setEmploymentStartDate(LocalDate.of(2024, 11, 01));//EmploymentStartDate

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println(
        String.format(Locale.US,
                "SELECT N'%s', N'%s', %d, %d, %.2f, %.2f, %s, GETDATE(), N'%s', GETDATE(), NULL, %s, %.2f, %.2f, %.2f, " +
                         "%.2f, %d, %d, %d, %d, %.2f, %.2f, %f, %.2f, '%s' " +
                        "\nUNION ALL \n",
                ResultWriter.apostroff(result.getUser().getName()),//UserShortName
                ResultWriter.apostroff(result.getUserPosition().getName()),//UserPosition
                result.getUserPosition().getMaxPoint(),//Point
                result.getUserPosition().getPointValue(),//PointValue
                result.getEmployment(),//Employment
                result.getHospNSZU_Premium()+ result.getAmblNSZU_Premium()+result.getOtherPremium()+ result.getOneDaySurgeryPremium(),//BasicPremium
                result.getDepartment().getDepartmentTemplateId(),//DepartmentTemplate
                ResultWriter.apostroff(result.getDepartment().getNameEleks()),//DepartmentName
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
                result.getOneDaySurgeryPremium(),//OneDaySurgery
                result.getOtherPremium(),//OtherPremium
                result.getEmploymentStartDate().format(formatter)//EmploymentStartDate
        ));

    }
}
