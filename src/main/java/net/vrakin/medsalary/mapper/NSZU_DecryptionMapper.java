package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.dto.NszuDecryptionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class NSZU_DecryptionMapper implements BaseMapper<NszuDecryption, NszuDecryptionDTO> {

    public abstract NszuDecryptionDTO toDto(NszuDecryption entity);



    public abstract NszuDecryption toEntity(NszuDecryptionDTO dto);
}