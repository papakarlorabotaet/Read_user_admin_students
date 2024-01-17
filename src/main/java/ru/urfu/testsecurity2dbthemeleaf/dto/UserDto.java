package ru.urfu.testsecurity2dbthemeleaf.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private List<String> roles;

    public String getNameWithoutRole() {
        String rolesString = getRoles().toString();
        String[] roles1 = rolesString.split("\\[");
        String roles2 = roles1[1];
        String[] arrayRoles2 = roles2.split("]");
        return arrayRoles2[0];
    }

    public String getNameWithoutRole1() {
        String[] roles = getRoles().toString().split("_");
        String roles1 = roles[1];
        String[] roles2 = roles1.split("]");
        return roles2[0];
    }


}
