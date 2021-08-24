package edu.pucmm.eict.soap.controllers;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ControllerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ShortUrlController.class).in(Singleton.class);
    }
}
