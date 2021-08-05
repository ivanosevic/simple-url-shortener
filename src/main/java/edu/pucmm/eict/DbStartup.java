package edu.pucmm.eict;

import edu.pucmm.eict.bootstrap.DataBootstrap;
import edu.pucmm.eict.bootstrap.DatabaseBootstrap;
import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.common.Startup;

public class DbStartup implements Startup {

    private ApplicationProperties appProperties = ApplicationProperties.getInstance();
    private DatabaseBootstrap dbBootstrap = DatabaseBootstrap.getInstance();

    @Override
    public void boot(String[] args) {
        // Try to make database embeddable
        boolean isEmbedded = appProperties.isEmbedded();
        int dbPort = appProperties.getH2Port();
        if(isEmbedded) {
            dbBootstrap.init(dbPort);
        }

        // Bootstrap initial data
        DataBootstrap dataBootstrap = DataBootstrap.getInstance();
        dataBootstrap.inserts();
    }
}
