package net.vrakin.medsalary.api;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.dto.NszuDecryptionDTO;
import net.vrakin.medsalary.exception.IdMismatchException;
import net.vrakin.medsalary.exception.ResourceExistException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.mapper.NSZU_DecryptionMapper;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import net.vrakin.medsalary.service.ServicePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST-контролер для управління розшифровками НСЗУ.
 *
 * Забезпечує CRUD-операції та додаткові запити за параметрами, такими як рік, місяць, виконавець тощо.
 */
@RestController
@RequestMapping("/api/nzsu_decryptions")
public class NSZU_DecryptionRestController {

    private final NSZU_DecryptionService nszuDecryptionService;
    private final NSZU_DecryptionMapper nszuDecryptionMapper;
    private final ServicePackageService servicePackageService;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param nszuDecryptionService сервіс для роботи з розшифровками НСЗУ.
     * @param nszuDecryptionMapper маппер для перетворення між DTO і сутностями.
     * @param servicePackageService сервіс для роботи з пакетами послуг.
     */
    @Autowired
    public NSZU_DecryptionRestController(NSZU_DecryptionService nszuDecryptionService,
                                         NSZU_DecryptionMapper nszuDecryptionMapper,
                                         ServicePackageService servicePackageService) {
        this.nszuDecryptionMapper = nszuDecryptionMapper;
        this.nszuDecryptionService = nszuDecryptionService;
        this.servicePackageService = servicePackageService;
    }

    /**
     * Отримати всі записи розшифровок НСЗУ.
     *
     * @return список усіх записів у форматі DTO.
     */
    @GetMapping
    public ResponseEntity<List<NszuDecryptionDTO>> getAll() {
        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper.toDtoList(nszuDecryptionService.findAll());
        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    /**
     * Отримати розшифровку НСЗУ за ідентифікатором.
     *
     * @param id ідентифікатор розшифровки.
     * @return DTO розшифровки.
     * @throws ResourceNotFoundException якщо розшифровку не знайдено.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NszuDecryptionDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        NszuDecryption nszuDecryption = nszuDecryptionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NSZU_Decryption", "id", id.toString()));

        NszuDecryptionDTO nszuDecryptionDto = nszuDecryptionMapper.toDto(nszuDecryption);

        return new ResponseEntity<>(nszuDecryptionDto, HttpStatus.OK);
    }

    /**
     * Отримати розшифровки НСЗУ за рік та місяць.
     *
     * @param year рік записів.
     * @param month місяць записів.
     * @return список розшифровок за заданий період.
     */
    @GetMapping("/date")
    public ResponseEntity<List<NszuDecryptionDTO>> getByYearAndMonth(@RequestParam int year,
                                                                     @RequestParam int month) {
        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper.toDtoList(
                nszuDecryptionService.findByYearNumAndMonthNum(year, month));

        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    /**
     * Отримати розшифровки НСЗУ за типом запису та періодом.
     *
     * @param recordKind тип запису.
     * @param year рік записів.
     * @param month місяць записів.
     * @return список розшифровок за типом запису та періодом.
     */
    @GetMapping("/recordKind/{recordKind}")
    public ResponseEntity<List<NszuDecryptionDTO>> getByRecordKind(@PathVariable String recordKind,
                                                                   @RequestParam int year, @RequestParam int month) {

        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper
                .toDtoList(nszuDecryptionService.findByRecordKind(recordKind)
                        .stream()
                        .filter(nszu_decryption -> ((nszu_decryption.getYearNum() == year)
                                && (nszu_decryption.getYearNum() == month)))
                        .toList());

        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    /**
     * Отримати розшифровки НСЗУ за місцем провайдера.
     *
     * @param providerPlace місце провайдера.
     * @param year рік записів.
     * @param month місяць записів.
     * @return список розшифровок за місцем провайдера та періодом.
     */
    @GetMapping("/providerPlace/{providerPlace}")
    public ResponseEntity<List<NszuDecryptionDTO>> getByProviderPlace(@PathVariable String providerPlace,
                                                                      @RequestParam int year, @RequestParam int month) {

        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper
                .toDtoList(nszuDecryptionService.findByProviderPlace(providerPlace)
                        .stream()
                        .filter(nszu_decryption -> ((nszu_decryption.getYearNum() == year)
                                && (nszu_decryption.getYearNum() == month)))
                        .toList());

        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    /**
     * Отримати розшифровки НСЗУ за пакетом послуг.
     *
     * @param servicePackageName назва пакета послуг.
     * @param year рік записів.
     * @param month місяць записів.
     * @return список розшифровок за пакетом послуг та періодом.
     * @throws ResourceNotFoundException якщо пакет послуг не знайдено.
     */
    @GetMapping("/servicePackage/{servicePackageName}")
    public ResponseEntity<List<NszuDecryptionDTO>> getByServicePackage(@PathVariable String servicePackageName,
                                                                       @RequestParam int year, @RequestParam int month) {

        ServicePackage servicePackage = servicePackageService.findByName(servicePackageName)
                .orElseThrow(() -> new ResourceNotFoundException("NSZU_Decryption", "servicePackageName", servicePackageName));
        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper
                .toDtoList(nszuDecryptionService.findByServicePackageName(servicePackage.getNumber())
                        .stream()
                        .filter(nszu_decryption -> ((nszu_decryption.getMonthNum() == year)
                                && (nszu_decryption.getMonthNum() == month)))
                        .toList());

        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    // Інші методи аналогічно можна прокоментувати детально
}
