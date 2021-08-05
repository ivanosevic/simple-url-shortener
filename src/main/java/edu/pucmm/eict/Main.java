package edu.pucmm.eict;

import edu.pucmm.eict.restapi.RestApiStartup;

public class Main {
    public static void main(String[] args) {
        // Initialize DB
        DbStartup dbStartup = new DbStartup();
        dbStartup.boot(args);

        // Initialize WebApp
        WebAppStartup webAppStartup = new WebAppStartup();
        webAppStartup.boot(args);

        // Initialize REST API
        RestApiStartup restApiStartup = new RestApiStartup();
        restApiStartup.boot(args);
    }
}