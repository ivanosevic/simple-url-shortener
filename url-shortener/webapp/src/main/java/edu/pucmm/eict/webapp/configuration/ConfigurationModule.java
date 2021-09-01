package edu.pucmm.eict.webapp.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import edu.pucmm.eict.webapp.converter.ShortUrlDtoConverter;
import org.modelmapper.ModelMapper;

import javax.inject.Singleton;

public class ConfigurationModule extends AbstractModule {

    @Provides
    @Singleton
    public ModelMapper getModelMapper(ShortUrlDtoConverter shortUrlDtoConverter) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(shortUrlDtoConverter);
        return modelMapper;
    }

    @Override
    protected void configure() {
        bind(CustomThymeleafRenderer.class).in(Singleton.class);
        bind(SecurityConfig.class).in(Singleton.class);
        bind(SessionFlash.class).in(Singleton.class);
    }
}
