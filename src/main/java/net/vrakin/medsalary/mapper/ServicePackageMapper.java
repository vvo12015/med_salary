package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.dto.ServicePackageDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class ServicePackageMapper implements BaseMapper<ServicePackage, ServicePackageDTO> {
    @Override
    public abstract ServicePackageDTO toDto(ServicePackage entity);

    @Override
    public abstract ServicePackage toEntity(ServicePackageDTO dto);
}