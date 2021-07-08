package edu.pucmm.eict;

import edu.pucmm.eict.bootstrap.DbBootstrap;
import edu.pucmm.eict.common.AppProperties;

import javax.inject.Inject;

public class DbStartup {

    private final AppProperties appProperties;
    private final DbBootstrap dbBootstrap;

    @Inject
    public DbStartup(AppProperties appProperties, DbBootstrap dbBootstrap) {
        this.appProperties = appProperties;
        this.dbBootstrap = dbBootstrap;
    }

    public void boot(String[] args) {
        boolean isEmbedded = appProperties.isEmbedded();
        int port = appProperties.getH2Port();
        if(isEmbedded) {
            dbBootstrap.init(port);
        }
    }
}
