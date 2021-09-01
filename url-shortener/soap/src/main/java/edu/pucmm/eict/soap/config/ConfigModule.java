package edu.pucmm.eict.soap.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import edu.pucmm.eict.soap.dto.ShortUrlDtoConverter;
import org.modelmapper.ModelMapper;

import javax.inject.Named;
import javax.inject.Singleton;

public class ConfigModule extends AbstractModule {

    @Provides
    @Singleton
    @Named("passwordEncryptor")
    public String getPasswordEncryptor() {
        return "wd583szUNc8bNZSA";
    }

    @Provides
    @Singleton
    @Named("persistenceUnit")
    public String getPersistenceUnit() {
        return "embedded";
    }

    @Provides
    @Singleton
    @Named("domain")
    public String getDomain() {
        return "short.wolfisc.com";
    }

    @Provides
    @Singleton
    @Named("webservicePort")
    public Integer getAppPort() {
        return 7002;
    }

    @Provides
    @Singleton
    public ModelMapper getModelMapper(ShortUrlDtoConverter shortUrlDtoConverter) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(shortUrlDtoConverter);
        return modelMapper;
    }

    @Override
    protected void configure() {
        super.configure();
    }
}
