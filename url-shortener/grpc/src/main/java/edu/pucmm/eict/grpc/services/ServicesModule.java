package edu.pucmm.eict.grpc.services;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExceptionHandler.class).in(Singleton.class);
        bind(ShortUrlServicesGrpc.class).in(Singleton.class);
    }
}
