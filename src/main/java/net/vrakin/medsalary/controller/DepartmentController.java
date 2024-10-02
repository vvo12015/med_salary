package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.dto.DepartmentDTO;
import net.vrakin.medsalary.excel.DepartmentExcelReader;
import net.vrakin.medsalary.excel.ExcelHelper;
import net.vrakin.medsalary.mapper.DepartmentMapper;
import net.vrakin.medsalary.mapper.StaffListRecordMapper;
import net.vrakin.medsalary.service.DepartmentService;
import net.vrakin.medsalary.service.StaffListRecordService;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/department")
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    private final StorageService storageService;

    private final DepartmentExcelReader departmentExcelReader;
    private final StaffListRecordMapper staffListRecordMapper;
    private final StaffListRecordService staffListRecordService;
    private final DepartmentMapper departmentMapper;

    public DepartmentController(DepartmentService departmentService, StorageService storageService,
                                DepartmentExcelReader departmentExcelReader, StaffListRecordMapper staffListRecordMapper,
                                StaffListRecordService staffListRecordService,
                                DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.storageService = storageService;
        this.departmentExcelReader = departmentExcelReader;
        this.staffListRecordMapper = staffListRecordMapper;
        this.staffListRecordService = staffListRecordService;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping
    public String departments(Model model){
        log.info("Accessing department page");

        model.addAttribute("files", storageService.loadAll().map(
                            path -> path.getFileName().toString())
                            .filter(f->f.startsWith("department"))
                            .collect(Collectors.toList()));

        model.addAttribute("stafflist", staffListRecordMapper.toDtoList(staffListRecordService.findAll()));
        model.addAttribute("departments", departmentMapper.toDtoList(departmentService.findAll()));

        return "department";
    }


    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        log.info("Accessing post files department page");

        int monthNumber = LocalDateTime.now().getMonthValue();
        int yearNumber = LocalDateTime.now().getYear();
        String savedFileName = String.format("%s_%d_%02d" + ExcelHelper.FILE_EXTENSION, "department", yearNumber, monthNumber);

        File destinationFile = storageService.store(file, savedFileName);

        String messageReadError = "";
        if (!departmentExcelReader.isValidateFile(destinationFile)) {
            messageReadError = " Error reading";
        }else{
            List<Department> departmentList = departmentExcelReader.readAllEntries(destinationFile).stream().distinct().collect(Collectors.toList());
            List<DepartmentDTO> departmentDTOList = departmentMapper.toDtoList(departmentList);

            departmentService.saveAll(departmentList);
            redirectAttributes.addFlashAttribute("departments"
                    , departmentDTOList);
        }
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + savedFileName + "!" + messageReadError);


        return "redirect:/department";
    }


}
