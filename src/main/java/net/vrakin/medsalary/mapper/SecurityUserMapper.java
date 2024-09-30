package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.SecurityUser;
import net.vrakin.medsalary.dto.SecurityUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class SecurityUserMapper implements BaseMapper<SecurityUser, SecurityUserDTO> {
    @Override
    public abstract SecurityUserDTO toDto(SecurityUser entity);

    @Override
    public abstract SecurityUser toEntity(SecurityUserDTO dto);
}