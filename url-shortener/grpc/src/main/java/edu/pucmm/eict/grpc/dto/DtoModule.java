package edu.pucmm.eict.grpc.dto;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class DtoModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ShortUrlDtoConverter.class).in(Singleton.class);
    }
}
