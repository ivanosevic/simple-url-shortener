package edu.pucmm.eict.webapp.controllers;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ControllerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AdminZoneController.class).in(Singleton.class);
        bind(AdviceController.class).in(Singleton.class);
        bind(AssignUrlsToUserFilter.class).in(Singleton.class);
        bind(AuthController.class).in(Singleton.class);
        bind(RedirectToAppFilter.class).in(Singleton.class);
        bind(RedirectUrlController.class).in(Singleton.class);
        bind(ShortUrlController.class).in(Singleton.class);
        bind(UrlStatisticsController.class).in(Singleton.class);
    }
}
