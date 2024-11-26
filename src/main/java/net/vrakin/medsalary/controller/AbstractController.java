package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.excel.entity.reader.ExcelHelper;
import net.vrakin.medsalary.excel.entity.reader.ExcelReader;
import net.vrakin.medsalary.mapper.BaseMapper;
import net.vrakin.medsalary.service.Service;
import net.vrakin.medsalary.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractController<E, D> {

    protected final String entityName;

    protected final StorageService storageService;

    protected final ExcelReader<E, D> excelReader;

    protected final Service<E> service;

    protected final BaseMapper<E, D> mapper;

    public AbstractController(String entityName, StorageService storageService,
                              ExcelReader<E, D> excelReader, Service<E> service, BaseMapper<E, D> mapper) {
        this.entityName = entityName;
        this.storageService = storageService;
        this.excelReader = excelReader;
        this.service = service;
        this.mapper = mapper;
    }

    protected void saveEntitiesAndSendDtoAndErrors(MultipartFile file, String monthYear, RedirectAttributes redirectAttributes) {
        YearMonth yearMonth = YearMonth.parse(monthYear);
        int monthNumber = yearMonth.getMonthValue();
        int yearNumber = yearMonth.getYear();

        LocalDate period = LocalDate.of(yearNumber, monthNumber, 01);

        String savedFileName = String.format("%s_%d_%02d" + ExcelHelper.FILE_EXTENSION, entityName, yearNumber, monthNumber);

        File destinationFile = storageService.storeUploadDir(file, savedFileName);

        String messageReadError = "";
        List<String> errorList = excelReader.isValidateFile(destinationFile);

        if (!errorList.isEmpty()) {
            messageReadError = errorList.stream().reduce((s, s2) -> s.concat(";\n Errors:\n" + s2)).toString();
        }else{
            log.info("Start save to DB");
            var DTOs = excelReader.readAllDto(destinationFile, period);
            var entities = mapper.toEntityList(DTOs);
            service.saveAll(entities);
            log.info("Finish save to DB");
            redirectAttributes.addFlashAttribute(entityName+"s", DTOs);
        }

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + savedFileName + "!" + messageReadError);
    }

    protected ResponseEntity<Resource> getFile(String filename) {
        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    protected void sendDto(Model model) {
        model.addAttribute("files", storageService.loadAll().map(
                        path -> path.getFileName().toString())
                .filter(f->f.startsWith(entityName))
                .collect(Collectors.toList()));

        model.addAttribute(entityName + "s", mapper.toDtoList(service.findAll()));
    }

    protected File getFileNameForSave(MultipartFile file, String monthYear) {
        YearMonth yearMonth = YearMonth.parse(monthYear);
        int monthNumber = yearMonth.getMonthValue();
        int yearNumber = yearMonth.getYear();
        String savedFileName = String.format("%s_%d_%02d" + ExcelHelper.FILE_EXTENSION, entityName, yearNumber, monthNumber);
        return storageService.storeUploadDir(file, savedFileName);
    }
}
