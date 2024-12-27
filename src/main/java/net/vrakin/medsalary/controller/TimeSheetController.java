package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.TimeSheet;
import net.vrakin.medsalary.dto.TimeSheetDTO;
import net.vrakin.medsalary.excel.entity.reader.TimeSheetExcelReader;
import net.vrakin.medsalary.mapper.TimeSheetMapper;
import net.vrakin.medsalary.service.StorageService;
import net.vrakin.medsalary.service.TimeSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequestMapping("/timesheet")
@Slf4j
public class TimeSheetController extends AbstractController<TimeSheet, TimeSheetDTO>{

    @Autowired
    public TimeSheetController(TimeSheetService timeSheetService, StorageService storageService,
                               TimeSheetMapper timeSheetMapper, TimeSheetExcelReader timeSheetExcelReader) {
        super("timesheet", storageService, timeSheetExcelReader, timeSheetService, timeSheetMapper);
    }

    @GetMapping
    public String userPosition(Model model){
        log.info("Accessing {} page", entityName);

        sendDto(model);
        if (Objects.nonNull(model.getAttribute(entityName))){
            log.info("entities is");
        }else{
            log.info("entities {} not exist", entityName);
        }

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

        log.info("Accessing post files {} page", entityName);

        saveEntitiesAndSendDtoAndErrors(file, monthYear, redirectAttributes);

        return "redirect:/" + entityName;
    }

}
