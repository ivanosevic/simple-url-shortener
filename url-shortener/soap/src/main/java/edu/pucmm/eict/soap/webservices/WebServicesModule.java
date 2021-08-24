package edu.pucmm.eict.soap.webservices;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class WebServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ShortUrlWebServices.class).in(Singleton.class);
    }
}
