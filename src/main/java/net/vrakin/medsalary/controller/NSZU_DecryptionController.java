package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.dto.NszuDecryptionDTO;
import net.vrakin.medsalary.excel.entity.reader.NszuDecryptionExcelReader;
import net.vrakin.medsalary.mapper.NSZU_DecryptionMapper;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/nszudecryption")
@Slf4j
public class NSZU_DecryptionController extends AbstractController<NszuDecryption, NszuDecryptionDTO> {

    @Autowired
    public NSZU_DecryptionController(StorageService storageService, NszuDecryptionExcelReader nszuDecryptionExcelReader,
                                     NSZU_DecryptionService nszu_decryptionService, NSZU_DecryptionMapper nszuDecryptionMapper
                                              ) {
        super("nszudecryption", storageService, nszuDecryptionExcelReader, nszu_decryptionService, nszuDecryptionMapper);
    }

    @GetMapping
    public String nszuDecryption(Model model){
        log.info("Accessing nszuDecryption page");

        sendDto(model);

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

        log.info("Accessing post files nszu_decryption page");

        saveEntitiesAndSendDtoAndErrors(file, monthYear, redirectAttributes);

        return "redirect:/" + entityName;
    }

}
