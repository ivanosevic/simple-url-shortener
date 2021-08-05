package edu.pucmm.eict.restapi.mappers;

import org.modelmapper.ModelMapper;

public class DtoMapper {

    private final ModelMapper modelMapper;
    private final UserLoginConverter userLoginConverter;

    private DtoMapper() {
        userLoginConverter = new UserLoginConverter();
        modelMapper = new ModelMapper();
        modelMapper.addConverter(userLoginConverter);
    }

    private static class InstanceHolder {
        public static DtoMapper instance = new DtoMapper();
    }

    public static DtoMapper getInstance() {
        return DtoMapper.InstanceHolder.instance;
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }
}
