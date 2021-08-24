package edu.pucmm.eict.webapp.sessionurls;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class SessionUrlModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SessionUrlService.class).in(Singleton.class);
    }
}
