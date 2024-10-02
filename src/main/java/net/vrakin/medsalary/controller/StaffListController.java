package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.dto.UserDTO;
import net.vrakin.medsalary.excel.ExcelHelper;
import net.vrakin.medsalary.excel.StaffListRecordExcelReader;
import net.vrakin.medsalary.generator.GeneratorStaffListRecordService;
import net.vrakin.medsalary.mapper.*;
import net.vrakin.medsalary.service.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/stafflist")
@Slf4j
public class StaffListController {

    public static final int ELEMENT_MAX_COUNT = 100;
    private final UserMapper userMapper;
    private final PremiumCategoryMapper premiumCategoryMapper;
    private UserService userService;
    private UserEleksService userEleksService;
    private UserPositionService userPositionService;
    private StaffListRecordService staffListRecordService;
    private DepartmentService departmentService;

    private final StorageService storageService;

    private final StaffListRecordExcelReader staffListRecordExcelReader;

    private StaffListRecordMapper staffListRecordMapper;

    private DepartmentMapper departmentMapper;

    private UserPositionMapper userPositionMapper;

    private PremiumCategoryService premiumCategoryService;

    private GeneratorStaffListRecordService generatorStaffListRecordService;

    public StaffListController(UserService userService, UserEleksService userEleksService, UserPositionService userPositionService,
                               StaffListRecordService staffListRecordService, DepartmentService departmentService,
                               StorageService storageService, StaffListRecordExcelReader staffListRecordExcelReader,
                               StaffListRecordMapper staffListRecordMapper, DepartmentMapper departmentMapper,
                               UserPositionMapper userPositionMapper, PremiumCategoryService premiumCategoryService,
                               GeneratorStaffListRecordService generatorStaffListRecordService, UserMapper userMapper, PremiumCategoryMapper premiumCategoryMapper) {
        this.userService = userService;
        this.userEleksService = userEleksService;
        this.userPositionService = userPositionService;
        this.staffListRecordService = staffListRecordService;
        this.departmentService = departmentService;
        this.storageService = storageService;
        this.staffListRecordExcelReader = staffListRecordExcelReader;
        this.staffListRecordMapper = staffListRecordMapper;
        this.departmentMapper = departmentMapper;
        this.userPositionMapper = userPositionMapper;
        this.premiumCategoryService = premiumCategoryService;
        this.generatorStaffListRecordService = generatorStaffListRecordService;
        this.userMapper = userMapper;
        this.premiumCategoryMapper = premiumCategoryMapper;
    }

    @GetMapping
    public String stafflist(Model model){
        log.info("Accessing new stafflist page");

        model.addAttribute("files", storageService.loadAll().map(
                        path -> path.getFileName().toString())
                .filter(f->f.startsWith("stafflist"))
                .collect(Collectors.toList()));

        List<StaffListRecord> staffListRecords = staffListRecordService.findAll();
        staffListRecords.forEach(st->log.info("staffRecordId: {}, name: {}", st.getId(), st.getUser().getName()));
        List<StaffListRecordDTO> stafflist = staffListRecordMapper.toDtoList(staffListRecordService.findAll());
        model.addAttribute("stafflist", stafflist.stream().limit(ELEMENT_MAX_COUNT));
        stafflist.forEach(st->log.info("staffRecordDTOId: {}, name: {}", st.getId(), st.getUser().getName()));
        model.addAttribute("userPositions", userPositionMapper.toDtoList(userPositionService.findAll()));
        model.addAttribute("departments", departmentMapper.toDtoList(departmentService.findAll()));
        model.addAttribute("premiumCategories", premiumCategoryMapper.toDtoList(premiumCategoryService.findAll()));
        List<UserDTO> userDTOList = userMapper.toDtoList(userService.findAll());
        int elementCount = userDTOList.size();
        model.addAttribute("users", userMapper.toDtoList(userService.findAll().subList(0, elementCount<100? elementCount:ELEMENT_MAX_COUNT)));

        return "stafflist";
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

        int monthNumber = LocalDate.now().getMonthValue();
        int yearNumber = LocalDate.now().getYear();
        String savedFileName = String.format("%s_%d_%02d" + ExcelHelper.FILE_EXTENSION, "staffList", yearNumber, monthNumber);

        File destinationFile = storageService.store(file, savedFileName);

        String messageReadError = "";
        if (!staffListRecordExcelReader.isValidateFile(destinationFile)) {
            messageReadError = " Error reading";
        }else {
            List<StaffListRecordDTO> staffListDTO = staffListRecordExcelReader.readAllDto(destinationFile);
            List<StaffListRecord> staffList = new ArrayList<>();
            for (StaffListRecordDTO staffListRecordDTO : staffListDTO) {
                StaffListRecord staffListRecord = generatorStaffListRecordService.generate(staffListRecordDTO);
                staffList.add(staffListRecord);
            }

            staffListRecordService.saveAll(staffList);

            redirectAttributes.addFlashAttribute("staffList",
                    staffListRecordMapper.toDtoList(staffListRecordService.findAll()));
        }
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + savedFileName + "!" + messageReadError);

        return "redirect:/stafflist";
    }
}
