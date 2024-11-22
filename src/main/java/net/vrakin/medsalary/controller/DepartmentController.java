package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.dto.DepartmentDTO;
import net.vrakin.medsalary.excel.entity.reader.DepartmentExcelReader;
import net.vrakin.medsalary.mapper.DepartmentMapper;
import net.vrakin.medsalary.mapper.StaffListRecordMapper;
import net.vrakin.medsalary.service.DepartmentService;
import net.vrakin.medsalary.service.StaffListRecordService;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.YearMonth;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/department")
@Slf4j
public class DepartmentController extends AbstractController<Department, DepartmentDTO>{

    private final StaffListRecordMapper staffListRecordMapper;
    private final StaffListRecordService staffListRecordService;

    public DepartmentController(StorageService storageService, DepartmentExcelReader departmentExcelReader,
                                DepartmentService departmentService,
                                StaffListRecordMapper staffListRecordMapper,
                                StaffListRecordService staffListRecordService,
                                DepartmentMapper departmentMapper) {
        super("department", storageService, departmentExcelReader, departmentService, departmentMapper);
        this.staffListRecordMapper = staffListRecordMapper;
        this.staffListRecordService = staffListRecordService;
    }

    @GetMapping
    public String departments(Model model){
        log.info("Accessing department page");

        sendDto(model);

        model.addAttribute("stafflist", staffListRecordMapper.toDtoList(staffListRecordService.findAll()));
        return entityName;
    }


    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        return getFile(filename);
    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam String monthYear,
                                   RedirectAttributes redirectAttributes) {

        log.info("Accessing post files department page");

        saveEntitiesAndSendDtoAndErrors(file, monthYear, redirectAttributes);

        service.saveAll(service.findAll().stream().map(d-> {
            d.setPeriod(YearMonth.parse(monthYear).atDay(1));
            return d;
        }).collect(Collectors.toList()));

        return "redirect:/" + entityName;
    }

}
