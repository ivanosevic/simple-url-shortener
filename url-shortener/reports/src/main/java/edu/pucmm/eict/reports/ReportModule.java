package edu.pucmm.eict.reports;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ReportModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AdminReportDao.class).in(Singleton.class);
        bind(UrlReportDao.class).in(Singleton.class);
    }
}
