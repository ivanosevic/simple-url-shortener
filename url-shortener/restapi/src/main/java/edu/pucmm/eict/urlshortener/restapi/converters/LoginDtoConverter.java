package edu.pucmm.eict.urlshortener.restapi.converters;

import edu.pucmm.eict.urlshortener.restapi.dtos.LoginDto;
import edu.pucmm.eict.urlshortener.users.Role;
import edu.pucmm.eict.urlshortener.users.User;
import org.modelmapper.AbstractConverter;

import java.util.List;
import java.util.stream.Collectors;

public class LoginDtoConverter extends AbstractConverter<User, LoginDto> {

    @Override
    protected LoginDto convert(User source) {
        List<String> roles = source.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        return new LoginDto(source.getName(), source.getLastname(), source.getUsername(), source.getEmail(), roles);
    }
}
