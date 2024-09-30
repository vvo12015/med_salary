package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.PremiumCategory;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.DTOStatus;
import net.vrakin.medsalary.dto.PremiumCategoryDTO;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class PremiumCategoryMapper implements BaseMapper<PremiumCategory, PremiumCategoryDTO> {
    @Override
    public PremiumCategoryDTO toDto(PremiumCategory entity){
        PremiumCategoryDTO premiumCategoryDTO = new PremiumCategoryDTO();

        premiumCategoryDTO.setId(entity.getId());
        premiumCategoryDTO.setName(entity.getName());
        premiumCategoryDTO.setStatus(DTOStatus.FROM_ENTITY);
        premiumCategoryDTO.setAmount(entity.getAmount());

        List<StaffListRecordDTO> staffListRecordDTOList = new ArrayList<>();
        for (StaffListRecord staffListRecord:
                entity.getStaffListRecords()){
            StaffListRecordDTO staffListRecordDTO = new StaffListRecordDTO();
            staffListRecordDTO.setId(staffListRecord.getId());

            staffListRecordDTOList.add(staffListRecordDTO);
        }
        premiumCategoryDTO.setStaffListRecords(staffListRecordDTOList);
        return premiumCategoryDTO;
    }

    @Override
    public abstract PremiumCategory toEntity(PremiumCategoryDTO dto);
}