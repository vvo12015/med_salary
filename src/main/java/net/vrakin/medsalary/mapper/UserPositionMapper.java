package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.UserPositionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class UserPositionMapper implements BaseMapper<UserPosition, UserPositionDTO> {
    @Override
    @Mapping(target = "period", source = "period")
    public abstract UserPositionDTO toDto(UserPosition entity);

    @Override
    @Mapping(target = "period", source = "period")
    public abstract UserPosition toEntity(UserPositionDTO dto);

}