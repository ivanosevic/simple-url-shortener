package edu.pucmm.eict.restapi.mappers;

import edu.pucmm.eict.restapi.dtos.LoginResponse;
import edu.pucmm.eict.users.Role;
import edu.pucmm.eict.users.User;
import org.modelmapper.AbstractConverter;

import java.util.List;
import java.util.stream.Collectors;

public class UserLoginConverter extends AbstractConverter<User, LoginResponse> {

    @Override
    protected LoginResponse convert(User source) {
        List<String> roles = source.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        return new LoginResponse(source.getName(), source.getLastname(), source.getUsername(), source.getEmail(), roles);
    }
}
