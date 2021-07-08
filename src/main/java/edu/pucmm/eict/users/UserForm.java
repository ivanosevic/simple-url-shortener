package edu.pucmm.eict.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserForm {
    private String email;
    private String username;
    private String password;
    private String name;
    private String lastname;
    private List<String> roles;
}