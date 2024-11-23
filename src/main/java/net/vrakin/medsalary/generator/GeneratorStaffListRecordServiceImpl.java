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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class GeneratorStaffListRecordServiceImpl implements GeneratorStaffListRecordService {
    private final UserPositionService userPositionService;
    private final DepartmentService departmentService;
    private final PremiumCategoryService premiumCategoryService;

    private final StaffListRecordService staffListRecordService;
    private final UserService userService;
    private final TimeSheetService timeSheetService;

    @Autowired
    public GeneratorStaffListRecordServiceImpl(UserPositionService userPositionService, DepartmentService departmentService,
                                               PremiumCategoryService premiumCategoryService,
                                               StaffListRecordService staffListRecordService,
                                               UserService userService,
                                               TimeSheetService timeSheetService) {
        this.userPositionService = userPositionService;
        this.departmentService = departmentService;
        this.premiumCategoryService = premiumCategoryService;
        this.staffListRecordService = staffListRecordService;
        this.userService = userService;
        this.timeSheetService = timeSheetService;
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

        //USER_POSITION
        UserPosition userPosition;
        List<UserPosition> userPositions = userPositionService
                .findByCodeIsProAndPeriod(staffListRecordDTO.getUserPosition().getCodeIsPro(), staffListRecordDTO.getPeriod());

        if (!userPositions.isEmpty())
        {
            userPosition = userPositions.stream().findFirst().get();
        }else if(Objects.nonNull(staffListRecordDTO.getUserPosition().getCodeIsPro())){
            throw new ResourceNotFoundException("userPosition", "name or codeIsPro", staffListRecordDTO
                                                                                    .getUserPosition()
                                                                                    .getCodeIsPro());
        }else {
            throw new ExcelFileErrorException("userPosition", "name or codeIsPro", "");
        }

        //DEPARTMENT
        Department department;
        Optional<Department> departmentOptional = Optional.empty();
        try {
            departmentOptional = departmentService.findByDepartmentIsProIdAndPeriod(
                    staffListRecordDTO.getDepartment().getDepartmentIsProId(), staffListRecordDTO.getPeriod());
        }catch (NonUniqueResultException e){
            log.error("departmentIsProId: {} , errorMessage: {}"
                    , staffListRecordDTO.getDepartment().getDepartmentIsProId(),
                    e.getMessage());
        }

        if (departmentOptional.isPresent())
        {
            department = departmentOptional.get();
        }else if(Objects.nonNull(staffListRecordDTO.getDepartment().getDepartmentIsProId())){
            throw new ResourceNotFoundException("department", "departmentIsProId",staffListRecordDTO.getDepartment().getDepartmentIsProId() );
        }else {
            throw new ExcelFileErrorException("department", "name or DepartmentIsProId", "");
        }

        //PREMIUM_CATEGORY
        PremiumCategory premiumCategory;
        String premiumCategoryName = Objects.requireNonNullElse(staffListRecordDTO.getPremiumCategoryName(), "ZERO");

        premiumCategory = premiumCategoryService.findByName(premiumCategoryName)
                .orElseThrow(() -> new ResourceNotFoundException("PremiumCategory", "premiumCategoryName", premiumCategoryName));

        //USER
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

        StaffListRecord entity = StaffListRecord.builder()
                .staffListId(staffListRecordDTO.getStaffListId())
                .userPosition(userPosition)
                .department(department)
                .employment(staffListRecordDTO.getEmployment())
                .user(user)
                .premiumCategory(premiumCategory)
                .employmentStartDate(staffListRecordDTO.getEmploymentStartDate())
                .employmentEndDate(staffListRecordDTO.getEmploymentEndDate())
                .startDate(staffListRecordDTO.getStartDate())
                .endDate(staffListRecordDTO.getEndDate())
                .salary(staffListRecordDTO.getSalary())
                .build();
        return entity;
    }

    private StaffListRecord getStaffListRecordFromCreateDTO(StaffListRecordDTO staffListRecordDTO) throws ResourceNotFoundException{
        StaffListRecord entity = null;
        if (staffListRecordDTO.getStatus().equals(DTOStatus.CREATE)) {
            if ((staffListRecordDTO.getId() != null)) throw new ResourceExistException("StaffListRecordDTO", "id");
            entity = StaffListRecord.builder()
                    .staffListId(staffListRecordDTO.getStaffListId())
                    .employment(staffListRecordDTO.getEmployment())
                    .employmentStartDate(staffListRecordDTO.getEmploymentStartDate())
                    .employmentEndDate(staffListRecordDTO.getEmploymentEndDate())
                    .startDate(staffListRecordDTO.getStartDate())
                    .endDate(staffListRecordDTO.getEndDate())
                    .salary(staffListRecordDTO.getSalary())
                    .build();
        }

        if (staffListRecordDTO.getStatus().equals(DTOStatus.EDIT)) {
            if ((staffListRecordDTO.getId() == null)) throw new ResourceNotFoundException("StaffList", "StaffListRecordDTO", "id");
            entity = staffListRecordService.findById(staffListRecordDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("StaffListRecordDTO", "findById"
                            , "null"));
            entity.setEmployment(staffListRecordDTO.getEmployment());
            entity.setEmploymentStartDate(staffListRecordDTO.getEmploymentStartDate());
            entity.setEmploymentEndDate(staffListRecordDTO.getEmploymentEndDate());
            entity.setStartDate(staffListRecordDTO.getStartDate());
            entity.setEndDate(staffListRecordDTO.getEndDate());
            entity.setSalary(staffListRecordDTO.getSalary());
        }

        if (entity == null) throw new ResourceNotFoundException("StaffListRecordDTO", "null", "null");

        //USER_POSITION
        UserPosition userPosition;
        if (Objects.isNull(staffListRecordDTO.getUserPositionId())) userPosition = null;
        else {
            userPosition = userPositionService.findById(staffListRecordDTO.getUserPositionId())
                    .orElseThrow(() -> new ResourceNotFoundException("StaffListRecordDTO", "userPositionId"
                            , staffListRecordDTO.getUserPosition().getId().toString()));
        }
        entity.setUserPosition(userPosition);

        //DEPARTMENT
        Department department = departmentService.findById(staffListRecordDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("StaffListRecordDTO", "departmentId"
                            , staffListRecordDTO.getDepartment().getId().toString()));

        entity.setDepartment(department);

        //PREMIUM_CATEGORY
        PremiumCategory premiumCategory;
        if (Objects.isNull(staffListRecordDTO.getPremiumCategoryName())) premiumCategory = null;
        else {
            premiumCategory = premiumCategoryService.findByName(staffListRecordDTO.getPremiumCategoryName())
                    .orElseThrow(() -> new ResourceNotFoundException("StaffListRecordDTO", "premiumCategoryId"
                            , staffListRecordDTO.getPremiumCategory().getId().toString()));
        }

        entity.setPremiumCategory(premiumCategory);

        //USER
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
