package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.UserPositionDTO;
import net.vrakin.medsalary.excel.entity.reader.UserPositionExcelReader;
import net.vrakin.medsalary.mapper.UserPositionMapper;
import net.vrakin.medsalary.service.StorageService;
import net.vrakin.medsalary.service.UserPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.YearMonth;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/userposition")
@Slf4j
public class UserPositionController extends AbstractController<UserPosition, UserPositionDTO>{

    @Autowired
    public UserPositionController(UserPositionService userPositionService, StorageService storageService,
                                  UserPositionMapper userPositionMapper, UserPositionExcelReader userPositionExcelReader) {
        super("userposition", storageService, userPositionExcelReader, userPositionService, userPositionMapper);
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
        service.saveAll(service.findAll().stream().map(up-> {
            up.setPeriod(YearMonth.parse(monthYear).atDay(1));
            return up;
        }).collect(Collectors.toList()));

        return "redirect:/" + entityName;
    }

}
