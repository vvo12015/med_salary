package net.vrakin.medsalary.controller;

import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.dto.*;
import net.vrakin.medsalary.excel.entity.reader.ExcelReader;
import net.vrakin.medsalary.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

@Controller
@RequestMapping("/one-window")
public class OneWindowsController {

    private ExcelReader<StaffListRecord, StaffListRecordDTO> staffListExcelReader;
    private ExcelReader<UserPosition, UserPositionDTO> userPositionExcelReader;
    private ExcelReader<Department, DepartmentDTO> departmentExcelReader;
    private ExcelReader<NszuDecryption, NszuDecryptionDTO> nszuDecryptionExcelReader;
    private ExcelReader<TimeSheet, TimeSheetDTO> timeSheetExcelReader;
    private ExcelReader<ServicePackage, ServicePackageDTO> servicePackageExcelReader;

    private StaffListRecordService staffListRecordService;

    private UserPositionService userPositionService;

    private DepartmentService departmentService;

    private NSZU_DecryptionService nszu_decryptionService;

    private TimeSheetService timeSheetService;

    private StorageService storageService;

    @Autowired
    public OneWindowsController(ExcelReader<StaffListRecord, StaffListRecordDTO> staffListExcelReader,
                                ExcelReader<UserPosition, UserPositionDTO> userPositionExcelReader,
                                ExcelReader<Department, DepartmentDTO> departmentExcelReader,
                                ExcelReader<NszuDecryption, NszuDecryptionDTO> nszuDecryptionExcelReader,
                                ExcelReader<TimeSheet, TimeSheetDTO> timeSheetExcelReader,
                                ExcelReader<ServicePackage, ServicePackageDTO> servicePackageExcelReader,
                                StaffListRecordService staffListRecordService,
                                StorageService storageService) {
        this.staffListExcelReader = staffListExcelReader;
        this.userPositionExcelReader = userPositionExcelReader;
        this.departmentExcelReader = departmentExcelReader;
        this.nszuDecryptionExcelReader = nszuDecryptionExcelReader;
        this.timeSheetExcelReader = timeSheetExcelReader;
        this.servicePackageExcelReader = servicePackageExcelReader;
        this.staffListRecordService = staffListRecordService;
        this.storageService = storageService;
}

    @GetMapping
    public String oneWindowPage(Model model){
        return "oneWindow";
    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("files") List<MultipartFile> files,
                                   @RequestParam String monthYear,
                                   RedirectAttributes redirectAttributes) throws IOException {

        List<String> messageErrors = new ArrayList<>();

        for (MultipartFile file: files) {
            File distinationFile = storageService.load(file.getName()).toFile();

            if (staffListExcelReader.isValidateFile(file.getResource().getFile()).isEmpty()){
                staffListRecordService.saveAll(staffListExcelReader.readAllEntries(file.getResource().getFile()));
                continue;
            }

            if (userPositionExcelReader.isValidateFile(file.getResource().getFile()).isEmpty()){
                userPositionService.saveAll(userPositionExcelReader.readAllEntries(file.getResource().getFile()));
                continue;
            }

            if (departmentExcelReader.isValidateFile(file.getResource().getFile()).isEmpty()){
                departmentService.saveAll(departmentExcelReader.readAllEntries(file.getResource().getFile()));
                continue;
            }

            if (nszuDecryptionExcelReader.isValidateFile(file.getResource().getFile()).isEmpty()){
                nszu_decryptionService.saveAll(nszuDecryptionExcelReader.readAllEntries(file.getResource().getFile()));
                continue;
            }

            if (timeSheetExcelReader.isValidateFile(file.getResource().getFile()).isEmpty()){
                timeSheetService.saveAll(timeSheetExcelReader.readAllEntries(file.getResource().getFile()));
                continue;
            }

            messageErrors.add(String.format("Error with file %d", file.getName()));
        }
        redirectAttributes.addAttribute("messageErrors", messageErrors);

        return "redirect:/one-window";
    }
}
