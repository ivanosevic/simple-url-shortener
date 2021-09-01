package edu.pucmm.eict.webapp.converter;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ConverterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ShortUrlDtoConverter.class).in(Singleton.class);
    }
}
