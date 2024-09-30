package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.excel.NszuDecryptionExcelReader;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
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
import java.time.YearMonth;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/nszu-decryption")
@Slf4j
public class NSZU_DecryptionController {

    private NSZU_DecryptionService nszu_decryptionService;

    private final StorageService storageService;

    private final NszuDecryptionExcelReader nszuDecryptionExcelReader;

    @Autowired
    public NSZU_DecryptionController(NSZU_DecryptionService nszu_decryptionService, StorageService storageService,
                                              NszuDecryptionExcelReader nszuDecryptionExcelReader) {
        this.nszu_decryptionService = nszu_decryptionService;
        this.storageService = storageService;
        this.nszuDecryptionExcelReader = nszuDecryptionExcelReader;
    }

    @GetMapping
    public String nszuDecryption(Model model){
        log.info("Accessing admin page");

        model.addAttribute("files", storageService.loadAll().filter(p->{
                    return ((p.getFileName().toString().startsWith("decryption")) ||
                            (p.getFileName().toString().startsWith("correct")));
                }).map(
                        path -> path.getFileName().toString())
                .collect(Collectors.toList()));

        model.addAttribute("msg_file", "file upload successfully");
        return "nszu_decryption";
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
                                   @RequestParam(value = "corrective", required = false) boolean corrective,
                                   @RequestParam String monthYear,
                                   RedirectAttributes redirectAttributes) {

        log.info("Accessing post files nszu_decryption page");

        YearMonth yearMonth = YearMonth.parse(monthYear);
        int monthNumber = yearMonth.getMonthValue();
        int yearNumber = yearMonth.getYear();
        String savedFileName = String.format("%s_%d_%02d.xslx", corrective?"correct":"decryption", yearNumber, monthNumber);

        if (storageService.load(savedFileName).toFile().exists()){
            storageService.delete(savedFileName);
        }

        File destinationFile = storageService.store(file, savedFileName);

        String messageReadError = "";
        if (!nszuDecryptionExcelReader.isValidateFile(destinationFile)) {
            messageReadError = " Error reading";
        }else{
            nszu_decryptionService.saveAll(nszuDecryptionExcelReader.readAllEntries(destinationFile));
            redirectAttributes.addFlashAttribute("nszuList", nszuDecryptionExcelReader.readAllDto(destinationFile));

        }
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + savedFileName + "!" + messageReadError);


        return "redirect:/nszu-decryption";
    }

}
