package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class UserMapper implements BaseMapper<User, UserDTO> {

    public UserDTO toDto(User entity){
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIpn(entity.getIpn());

        return dto;
    }

    public abstract User toEntity(UserDTO dto);

}