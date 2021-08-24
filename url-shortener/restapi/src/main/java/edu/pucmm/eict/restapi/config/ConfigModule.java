package edu.pucmm.eict.restapi.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import edu.pucmm.eict.restapi.dtos.LoginDtoConverter;
import edu.pucmm.eict.restapi.dtos.ShortUrlDtoConverter;
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
    @Named("jwtSecret")
    public String getJwtSecret() {
        return "dDgHtpN5Hpfehzku";
    }

    @Provides
    @Singleton
    @Named("jwtExpiration")
    public Integer getJwtExpiration() {
        return 86400000;
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
    @Named("apiPort")
    public Integer getApiPort() {
        return 7001;
    }

    @Provides
    @Singleton
    public ObjectMapper getJacksonMapper() {
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Provides
    @Singleton
    public ModelMapper getModelMapper(LoginDtoConverter loginDtoConverter, ShortUrlDtoConverter shortUrlDtoConverter) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(loginDtoConverter);
        modelMapper.addConverter(shortUrlDtoConverter);
        return modelMapper;
    }

    @Override
    protected void configure() {
        bind(JwtUtils.class).in(Singleton.class);
        bind(SecurityConfig.class).in(Singleton.class);
    }
}
