package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.UserPositionDTO;
import net.vrakin.medsalary.excel.ExcelHelper;
import net.vrakin.medsalary.excel.UserPositionExcelReader;
import net.vrakin.medsalary.mapper.UserPositionMapper;
import net.vrakin.medsalary.service.StorageService;
import net.vrakin.medsalary.service.UserPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user-position")
@Slf4j
public class UserPositionController {

    private final UserPositionService userPositionService;

    private final StorageService storageService;

    private final UserPositionMapper userPositionMapper;

    private final UserPositionExcelReader userPositionExcelReader;

    @Autowired
    public UserPositionController(UserPositionService userPositionService, StorageService storageService,
                                  UserPositionMapper userPositionMapper, UserPositionExcelReader userPositionExcelReader) {
        this.userPositionService = userPositionService;
        this.storageService = storageService;
        this.userPositionMapper = userPositionMapper;
        this.userPositionExcelReader = userPositionExcelReader;
    }

    @GetMapping
    public String user(Model model){
        log.info("Accessing admin page");
        model.addAttribute("files", storageService.loadAll().map(
                        path -> path.getFileName().toString())
                .filter(f->f.startsWith("user_positions"))
                .collect(Collectors.toList()));
        List<UserPosition> userPositions = userPositionService.findAll();
        model.addAttribute("userPositions", userPositionService.findAll());
        userPositions.stream().forEach(up->log.info(up.toString()));
        return "userPosition";
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

        log.info("Accessing post files userPosition page");

        int monthNumber = LocalDateTime.now().getMonthValue();
        int yearNumber = LocalDateTime.now().getYear();
        String savedFileName = String.format("%s_%d_%02d" + ExcelHelper.FILE_EXTENSION, "user_position", yearNumber, monthNumber);

        File destinationFile = storageService.store(file, savedFileName);

        String messageReadError = "";
        if (!userPositionExcelReader.isValidateFile(destinationFile)) {
            messageReadError = " Error reading";
        }else{
            List<UserPosition> userPositionList = userPositionExcelReader.readAllEntries(destinationFile);
            List<UserPositionDTO> userPositionDTOList = userPositionMapper.toDtoList(userPositionList);

            userPositionService.saveAll(userPositionList);
            redirectAttributes.addFlashAttribute("userPositions"
                    , userPositionMapper.toDtoList(userPositionService.findAll()));
        }
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + savedFileName + "!" + messageReadError);


        return "redirect:/user-position";
    }

}
