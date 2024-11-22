package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.dto.UserDTO;
import net.vrakin.medsalary.excel.entity.reader.StaffListRecordExcelReader;
import net.vrakin.medsalary.mapper.*;
import net.vrakin.medsalary.service.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/stafflist")
@Slf4j
public class StaffListController extends AbstractController<StaffListRecord, StaffListRecordDTO>{

    public static final int ELEMENT_MAX_COUNT = 100;
    private final UserMapper userMapper;
    private final PremiumCategoryMapper premiumCategoryMapper;
    private final UserService userService;
    private final UserPositionService userPositionService;

    private final DepartmentService departmentService;

    private final DepartmentMapper departmentMapper;

    private final UserPositionMapper userPositionMapper;

    private final PremiumCategoryService premiumCategoryService;

    public StaffListController(UserService userService, UserPositionService userPositionService,
                               StaffListRecordService staffListRecordService, DepartmentService departmentService,
                               StorageService storageService, StaffListRecordExcelReader staffListRecordExcelReader,
                               StaffListRecordMapper staffListRecordMapper, DepartmentMapper departmentMapper,
                               UserPositionMapper userPositionMapper, PremiumCategoryService premiumCategoryService,
                               UserMapper userMapper, PremiumCategoryMapper premiumCategoryMapper) {
        super("stafflist", storageService, staffListRecordExcelReader, staffListRecordService, staffListRecordMapper);
        this.userService = userService;
        this.userPositionService = userPositionService;
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
        this.userPositionMapper = userPositionMapper;
        this.premiumCategoryService = premiumCategoryService;
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

        List<StaffListRecordDTO> stafflist = mapper.toDtoList(service.findAll());
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

        return getFile(filename);
    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam String monthYear,
                                   RedirectAttributes redirectAttributes) {

        saveEntitiesAndSendDtoAndErrors(file, monthYear, redirectAttributes);

        service.saveAll(service.findAll().stream().map(sl-> {
            sl.setStartDate(YearMonth.parse(monthYear).atDay(1).atTime(0, 0));
            return sl;
        }).collect(Collectors.toList()));

        List<UserPosition> userPositions = userPositionService.findAll();
        List<Department> departments = departmentService.findAll();

        redirectAttributes.addFlashAttribute("userPositions", userPositions);
        redirectAttributes.addFlashAttribute("departments", departments);

        return "redirect:/" + entityName;
    }

}
