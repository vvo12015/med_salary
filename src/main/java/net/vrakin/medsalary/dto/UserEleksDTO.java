package net.vrakin.medsalary.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserEleksDTO {

    private Long id;

    private String login;

    private UserDTO userDTO;

    private DTOStatus status;
}
