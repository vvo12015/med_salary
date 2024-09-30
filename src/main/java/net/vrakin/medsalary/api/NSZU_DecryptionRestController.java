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

import java.util.List;

@RestController
@RequestMapping("/api/nzsu_decryptions")
public class NSZU_DecryptionRestController {

    private NSZU_DecryptionService nszuDecryptionService;
    private NSZU_DecryptionMapper nszuDecryptionMapper;

    private ServicePackageService servicePackageService;

    @Autowired
    public NSZU_DecryptionRestController(NSZU_DecryptionService nszuDecryptionService,
                                     NSZU_DecryptionMapper nszuDecryptionMapper,
                                     ServicePackageService servicePackageService) {
        this.nszuDecryptionMapper = nszuDecryptionMapper;
        this.nszuDecryptionService = nszuDecryptionService;
        this.servicePackageService = servicePackageService;
    }

    @GetMapping
    public ResponseEntity<List<NszuDecryptionDTO>> getAll() {
        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper.toDtoList(nszuDecryptionService.findAll());
        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NszuDecryptionDTO> getById(@PathVariable Long id) throws ResourceNotFoundException {
        NszuDecryption nszuDecryption = nszuDecryptionService.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("NSZU_Decryption", "id", id.toString()));

        NszuDecryptionDTO nszuDecryptionDto = nszuDecryptionMapper.toDto(nszuDecryption);

        return new ResponseEntity<>(nszuDecryptionDto, HttpStatus.OK);
    }

    @GetMapping("/date")
    public ResponseEntity<List<NszuDecryptionDTO>> getByYearAndMonth(@RequestParam int year,
                                                                     @RequestParam int month){
        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper.toDtoList(
                nszuDecryptionService.findByYearAndMonth(year, month));

        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    @GetMapping("/recordKind/{recordKind}")
    public ResponseEntity<List<NszuDecryptionDTO>> getByRecordKind(@PathVariable String recordKind,
                                                                   @RequestParam int year, @RequestParam int month) {

        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper
                .toDtoList(nszuDecryptionService.findByRecordKind(recordKind).stream().filter(nszu_decryption -> {
                            return ((nszu_decryption.getYear()==year)
                                    && (nszu_decryption.getYear()==month));
                        })
                        .toList());

        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    @GetMapping("/providerPlace/{providerPlace}")
    public ResponseEntity<List<NszuDecryptionDTO>> getByProviderPlace(@PathVariable String providerPlace,
                                                                      @RequestParam int year, @RequestParam int month){

        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper
                .toDtoList(nszuDecryptionService.findByProviderPlace(providerPlace).stream().filter(nszu_decryption -> {
                            return ((nszu_decryption.getYear()==year)
                                    && (nszu_decryption.getYear()==month));
                        })
                        .toList());

        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    @GetMapping("/servicePackage/{servicePackage}")
    public ResponseEntity<List<NszuDecryptionDTO>> getByServicePackage(@PathVariable String servicePackageName,
                                                                       @RequestParam int year, @RequestParam int month){

        ServicePackage servicePackage = servicePackageService.findByName(servicePackageName)
                .orElseThrow(()->new ResourceNotFoundException("NSZU_Decryption", "servicePackageName", servicePackageName));
        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper
                .toDtoList(nszuDecryptionService.findByServicePackageName(servicePackage.getNumber()).stream().filter(nszu_decryption -> {
                            return ((nszu_decryption.getYear()==year)
                                    && (nszu_decryption.getMonth()==month));
                        })
                        .toList());

        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    @GetMapping("/executorName/{executorName}")
    public ResponseEntity<List<NszuDecryptionDTO>> getByExecutorName(@PathVariable String executorName,
                                                                     @RequestParam int year, @RequestParam int month){

        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper
                .toDtoList(nszuDecryptionService.findByExecutorName(executorName));

        if ((year != 0) && (month != 0)){
            nszuDecryptionDtoList = nszuDecryptionDtoList.stream().filter(nszu_decryption -> {
                        return ((nszu_decryption.getYear()==year)
                                && (nszu_decryption.getMonth()==month));
                    })
                    .toList();
        }

        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }

    @GetMapping("/executorName-userPosition/{executorName}/{userPosition}")
    public ResponseEntity<List<NszuDecryptionDTO>> getByExecutorNameAndUserPosition(
            @PathVariable String executorName, @PathVariable String userPosition,
            @RequestParam int year, @RequestParam int month){

        List<NszuDecryptionDTO> nszuDecryptionDtoList = nszuDecryptionMapper
                .toDtoList(nszuDecryptionService.findByExecutorNameAndExecutorUserPosition(executorName, userPosition));

        if ((year != 0) && (month != 0)){
            nszuDecryptionDtoList = nszuDecryptionDtoList.stream().filter(nszu_decryption -> {
                        return ((nszu_decryption.getYear()==year)
                                && (nszu_decryption.getMonth()==month));
                    })
                    .toList();
        }

        return new ResponseEntity<>(nszuDecryptionDtoList, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<NszuDecryptionDTO> add(@RequestBody NszuDecryptionDTO nszuDecryptionDTO) {

        if (nszuDecryptionDTO.getId() != null) {
            throw new ResourceExistException("NSZU_DecryptionDTO", nszuDecryptionDTO.getId().toString());
        }

        NszuDecryption nszuDecryption = nszuDecryptionMapper.toEntity(nszuDecryptionDTO);

        NszuDecryptionDTO savedNSZUDecryption = nszuDecryptionMapper.toDto(nszuDecryptionService.save(nszuDecryption));

        return new ResponseEntity<>(savedNSZUDecryption, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            nszuDecryptionService.deleteById(id);
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("NSZU_Decryption", "id", id.toString());
        }
        return ResponseEntity.ok("NSZU_Decryption deleted successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<NszuDecryptionDTO> updateUser(@PathVariable Long id, @RequestBody NszuDecryptionDTO nszuDecryptionDTO) {

        if (!nszuDecryptionDTO.getId().equals(id)){
            throw new IdMismatchException("NSZUDecryptionDTO", id.toString(), nszuDecryptionDTO.getId().toString());
        }

        nszuDecryptionService.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("NSZUDecryption", "id", id.toString()));


        NszuDecryption nszuDecryption = nszuDecryptionMapper.toEntity(nszuDecryptionDTO);


        NszuDecryptionDTO savedNSZU_DecryptionDTO = nszuDecryptionMapper.toDto(nszuDecryptionService.save(nszuDecryption));

        return new ResponseEntity<>(savedNSZU_DecryptionDTO, HttpStatus.OK);
    }
}
