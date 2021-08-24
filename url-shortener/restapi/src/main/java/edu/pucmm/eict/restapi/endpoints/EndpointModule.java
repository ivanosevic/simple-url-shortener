package edu.pucmm.eict.restapi.endpoints;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class EndpointModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AuthEndpoint.class).in(Singleton.class);
        bind(ShortUrlEndpoint.class).in(Singleton.class);
    }
}
