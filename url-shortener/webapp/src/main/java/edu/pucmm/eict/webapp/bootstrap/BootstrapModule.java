package edu.pucmm.eict.webapp.bootstrap;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class BootstrapModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataBootstrap.class).to(DataBootstrapImpl.class).in(Singleton.class);
        bind(EmbeddedDb.class).to(H2Database.class).in(Singleton.class);
    }
}
