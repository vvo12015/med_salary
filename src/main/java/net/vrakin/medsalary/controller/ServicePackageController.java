package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.dto.ServicePackageDTO;
import net.vrakin.medsalary.excel.entity.reader.ServicePackageExcelReader;
import net.vrakin.medsalary.mapper.ServicePackageMapper;
import net.vrakin.medsalary.service.ServicePackageService;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/service-package")
@Slf4j
public class ServicePackageController extends AbstractController<ServicePackage, ServicePackageDTO> {


    @Autowired
    public ServicePackageController(StorageService storageService,
                                    ServicePackageExcelReader servicePackageExcelReader,
                                    ServicePackageService servicePackageService,
                                    ServicePackageMapper servicePackageMapper) {
        super("service-package", storageService, servicePackageExcelReader, servicePackageService, servicePackageMapper);
    }

    @GetMapping
    public String servicePackage(Model model){
        log.info("Accessing service package page");

        model.addAttribute("files", storageService
                .loadAll()
                .filter(p-> ((p.getFileName().toString().startsWith(entityName))))
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList()));

        model.addAttribute("msg_file", "file upload successfully");
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
