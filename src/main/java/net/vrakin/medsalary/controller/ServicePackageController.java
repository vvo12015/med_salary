package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.excel.ServicePackageExcelReader;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import net.vrakin.medsalary.service.ServicePackageService;
import net.vrakin.medsalary.service.StorageService;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/service-package")
@Slf4j
public class ServicePackageController {
    private NSZU_DecryptionService nszu_decryptionService;

    private final StorageService storageService;

    private final ServicePackageExcelReader servicePackageExcelReader;
    private ServicePackageService servicePackageService;

    @Autowired
    public ServicePackageController(NSZU_DecryptionService nszu_decryptionService, StorageService storageService,
                                    ServicePackageExcelReader servicePackageExcelReader,
                                    ServicePackageService servicePackageService) {
        this.nszu_decryptionService = nszu_decryptionService;
        this.storageService = storageService;
        this.servicePackageExcelReader = servicePackageExcelReader;
        this.servicePackageService = servicePackageService;
    }

    @GetMapping
    public String servicePackage(Model model){
        log.info("Accessing service package page");

        model.addAttribute("files", storageService.loadAll().filter(p->{
                    return ((p.getFileName().toString().startsWith("servicePackage")));
                }).map(
                        path -> path.getFileName().toString())
                .collect(Collectors.toList()));

        model.addAttribute("msg_file", "file upload successfully");
        return "service_package";
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

        log.info("Accessing post files service package page");

        int monthNumber = LocalDateTime.now().getMonthValue();
        int yearNumber = LocalDateTime.now().getYear();

        String savedFileName = String.format("%s_%d_%02d.xslx", "servicePackage", yearNumber, monthNumber);

        if (storageService.load(savedFileName).toFile().exists()){
            storageService.delete(savedFileName);
        }

        File destinationFile = storageService.store(file, savedFileName);

        String messageReadError = "";
        if (!servicePackageExcelReader.isValidateFile(destinationFile)) {
            messageReadError = " Error reading";
        }else{
            servicePackageService.saveAll(servicePackageExcelReader.readAllEntries(destinationFile));
            redirectAttributes.addFlashAttribute("servicePackageList", servicePackageExcelReader.readAllDto(destinationFile));

        }
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + savedFileName + "!" + messageReadError);


        return "redirect:/nszu-decryption";
    }

}
