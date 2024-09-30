package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.UserPositionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class UserPositionMapper implements BaseMapper<UserPosition, UserPositionDTO> {
    @Override
    public abstract UserPositionDTO toDto(UserPosition entity);

    @Override
    public abstract UserPosition toEntity(UserPositionDTO dto);
}