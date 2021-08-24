package edu.pucmm.eict.restapi.dtos;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class DtoModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LoginDtoConverter.class).in(Singleton.class);
        bind(ShortUrlDtoConverter.class).in(Singleton.class);
    }
}
