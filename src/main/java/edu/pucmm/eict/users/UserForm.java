package edu.pucmm.eict.users;

import edu.pucmm.eict.common.validation.UniqueEmail;
import edu.pucmm.eict.common.validation.UniqueUsername;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserForm {

    @UniqueUsername
    @NotBlank(message = "Username can't be empty.")
    @Size(min = 3, max = 50, message = "Username has to be between 3 and 50 characters.")
    private String username;

    @NotBlank(message = "Password can't be empty.")
    @Size(min = 3, max = 16, message = "Password has to be between 3 and 16 characters.")
    private String password;

    @Email
    @UniqueEmail
    private String email;

    @NotBlank(message = "Name can't be empty.")
    @Size(min = 3, max = 100, message = "Name has to be between 3 and 100 characters.")
    private String name;

    @NotBlank(message = "Lastname can't be empty.")
    @Size(min = 3, max = 100, message = "Lastname has to be between 3 and 100 characters.")
    private String lastname;

    private Set<String> roles;

    public UserForm() {
    }

    public UserForm(String username, String password, String email, String name, String lastname, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", roles=" + roles +
                '}';
    }
}