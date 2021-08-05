package edu.pucmm.eict.restapi.dtos;

import java.util.List;

public class LoginResponse {
    private String token;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private List<String> roles;

    public LoginResponse(String name, String lastname, String username, String email, List<String> roles) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public LoginResponse(String token, String name, String lastname, String username, String email, List<String> roles) {
        this.token = token;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
