package net.vrakin.medsalary.generator;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.dto.DTOStatus;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.exception.ExcelFileErrorException;
import net.vrakin.medsalary.exception.ResourceExistException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.service.*;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class GeneratorStaffListRecordServiceImpl implements Generator<StaffListRecordDTO, StaffListRecord>, GeneratorStaffListRecordService {
    private final UserPositionService userPositionService;
    private final DepartmentService departmentService;
    private final PremiumCategoryService premiumCategoryService;

    private final StaffListRecordService staffListRecordService;
    private final UserService userService;

    @Autowired
    public GeneratorStaffListRecordServiceImpl(UserPositionService userPositionService, DepartmentService departmentService,
                                               PremiumCategoryService premiumCategoryService,
                                               StaffListRecordService staffListRecordService,
                                               UserService userService) {
        this.userPositionService = userPositionService;
        this.departmentService = departmentService;
        this.premiumCategoryService = premiumCategoryService;
        this.staffListRecordService = staffListRecordService;
        this.userService = userService;
    }

    @Override
    public StaffListRecord generate(StaffListRecordDTO staffListRecordDTO){

        StaffListRecord entity;

        if (Objects.requireNonNullElse(staffListRecordDTO.getStatus(), DTOStatus.CREATE).equals(DTOStatus.FROM_FILE)){
           entity = getStaffListRecordFromFile(staffListRecordDTO);
        } else {
            entity = getStaffListRecordFromCreateDTO(staffListRecordDTO);
        }

        return entity;
    }

    private StaffListRecord getStaffListRecordFromFile(StaffListRecordDTO staffListRecordDTO) {
        StaffListRecord entity = new StaffListRecord();

        entity.setStaffListId(staffListRecordDTO.getStaffListId());

        UserPosition userPosition;
        List<UserPosition> userPositions = userPositionService
                .findByCodeIsPro(staffListRecordDTO.getUserPosition().getCodeIsPro());

        if (!userPositions.isEmpty())
        {
            userPosition = userPositions.stream().findFirst().get();

        }else if(Objects.nonNull(staffListRecordDTO.getUserPosition().getCodeIsPro())){
            userPosition = new UserPosition();
            userPosition.setCodeIsPro(staffListRecordDTO.getUserPosition().getCodeIsPro());
            if (Objects.nonNull(staffListRecordDTO.getUserPosition().getName())) {
                userPosition.setName(staffListRecordDTO.getUserPosition().getName());
            }
            userPosition = userPositionService.save(userPosition);
            log.info("Create userPosition: {}", userPosition.toString());
        }else {
            throw new ExcelFileErrorException("userPosition", "name or codeIsPro", "");
        }

        entity.setUserPosition(userPosition);

        Department department;
        Optional<Department> departmentOptional = Optional.empty();
        try {
            departmentOptional = departmentService.findByDepartmentIsProId(
                    staffListRecordDTO.getDepartment().getDepartmentIsProId());
        }catch (NonUniqueResultException e){
            log.error("departmentIsProId: {} , errorMessage: {}"
                    , staffListRecordDTO.getDepartment().getDepartmentIsProId(),
                    e.getMessage());
        }


        if (departmentOptional.isPresent())
        {
            department = departmentOptional.get();
        }else if(Objects.nonNull(staffListRecordDTO.getDepartment().getDepartmentIsProId())){
            department = new Department();
            department.setDepartmentIsProId(staffListRecordDTO.getDepartment().getDepartmentIsProId());
            if (Objects.nonNull(staffListRecordDTO.getDepartment().getName())) {
                department.setName(staffListRecordDTO.getDepartment().getName());
            }

            department = departmentService.save(department);
            log.info(department.toString());
        }else {
            throw new ExcelFileErrorException("department", "name or DepartmentIsProId", "");
        }

        entity.setDepartment(department);
        entity.setEmployment(staffListRecordDTO.getEmployment());

        String premiumCategoryName = Objects.requireNonNullElse(staffListRecordDTO.getPremiumCategoryName(), "ZERO");

        entity.setPremiumCategory(premiumCategoryService.findByName(premiumCategoryName)
                .orElseThrow(() -> new ResourceNotFoundException("PremiumCategory", "premiumCategoryName", premiumCategoryName)));


        User user;
        Optional<User> userOptional = userService.findByIPN(staffListRecordDTO.getUser().getIpn());

        if (userOptional.isPresent())
        {
            user = userOptional.get();
        }else if(Objects.nonNull(staffListRecordDTO.getUser().getIpn())){
            user = new User();
            if (Objects.nonNull(staffListRecordDTO.getUser().getName())) {
                user.setName(staffListRecordDTO.getUser().getName());
            }
            user.setIpn(staffListRecordDTO.getUser().getIpn());
            user = userService.save(user);
            log.info(user.toString());
        }else {
            throw new ExcelFileErrorException("user", "name or IPN", "");
        }
        log.info("UserPosition: {}, Department: {}, User: {}", userPosition.getName(), department.getName(), user.getName());
        entity.setUser(user);
        return entity;
    }

    private StaffListRecord getStaffListRecordFromCreateDTO(StaffListRecordDTO staffListRecordDTO) throws ResourceNotFoundException{
        StaffListRecord entity = null;
        if (staffListRecordDTO.getStatus().equals(DTOStatus.CREATE)) {
            if ((staffListRecordDTO.getId() != null)) throw new ResourceExistException("StaffListRecordDTO", "id");
            entity = new StaffListRecord();
        }

        if (staffListRecordDTO.getStatus().equals(DTOStatus.EDIT)) {
            if ((staffListRecordDTO.getId() == null)) throw new ResourceNotFoundException("StaffList", "StaffListRecordDTO", "id");
            entity = staffListRecordService.findById(staffListRecordDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("StaffListRecordDTO", "findById"
                            , "null"));
        }

        if (entity == null) throw new ResourceNotFoundException("StaffListRecordDTO", "null", "null");

        entity.setStaffListId(staffListRecordDTO.getStaffListId());

        UserPosition userPosition;
        if (Objects.isNull(staffListRecordDTO.getUserPositionId())) userPosition = null;
        else {
            userPosition = userPositionService.findById(staffListRecordDTO.getUserPositionId())
                    .orElseThrow(() -> new ResourceNotFoundException("StaffListRecordDTO", "userPositionId"
                            , staffListRecordDTO.getUserPosition().getId().toString()));
        }
        entity.setUserPosition(userPosition);

        Department department = departmentService.findById(staffListRecordDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("StaffListRecordDTO", "departmentId"
                            , staffListRecordDTO.getDepartment().getId().toString()));

        entity.setDepartment(department);
        PremiumCategory premiumCategory;
        if (Objects.isNull(staffListRecordDTO.getPremiumCategoryName())) premiumCategory = null;
        else {
            premiumCategory = premiumCategoryService.findByName(staffListRecordDTO.getPremiumCategoryName())
                    .orElseThrow(() -> new ResourceNotFoundException("StaffListRecordDTO", "premiumCategoryId"
                            , staffListRecordDTO.getPremiumCategory().getId().toString()));
        }

        entity.setPremiumCategory(premiumCategory);

        User user = null;
        if (Objects.nonNull(staffListRecordDTO.getUserId())){
            user = userService.findById(staffListRecordDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("StaffListRecordDTO", "userId"
                            , staffListRecordDTO.getUser().getId().toString()));
        }

        entity.setUser(user);
        return entity;
    }
}
