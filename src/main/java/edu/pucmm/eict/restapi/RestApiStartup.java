package edu.pucmm.eict.restapi;

import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.restapi.endpoints.ErrorControllerAdvice;
import edu.pucmm.eict.restapi.endpoints.LoginEndpoint;
import edu.pucmm.eict.restapi.endpoints.TestEndpoint;
import edu.pucmm.eict.restapi.security.ApiSecurity;
import io.javalin.Javalin;

public class RestApiStartup {

    public void boot(String[] args) {
        ApplicationProperties appProperties = ApplicationProperties.getInstance();
        int port = appProperties.getRestapiPort();
        Javalin restApiApp = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
            config.showJavalinBanner = false;
        });

        restApiApp.start(port);

        new ApiSecurity(restApiApp).applyConfiguration();
        new ErrorControllerAdvice(restApiApp).applyRoutes();
        new TestEndpoint(restApiApp).applyRoutes();
        new LoginEndpoint(restApiApp).applyRoutes();
    }
}
