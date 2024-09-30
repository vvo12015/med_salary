package net.vrakin.medsalary.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SecurityUserDTO {
    private Long id;
    private String login;
    private String password;
    private String securityRole;
    private String email;
    private String phone;
    private String address;
}
