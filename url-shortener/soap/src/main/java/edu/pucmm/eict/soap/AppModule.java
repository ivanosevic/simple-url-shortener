package edu.pucmm.eict.soap;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import edu.pucmm.eict.soap.config.ConfigModule;
import edu.pucmm.eict.soap.controllers.ControllerModule;
import edu.pucmm.eict.soap.webservices.WebServicesModule;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

import javax.inject.Singleton;

public class AppModule extends AbstractModule {

    @Provides
    @Singleton
    public Javalin getJavalinInstance() {
        return Javalin.create(JavalinConfig::enableCorsForAllOrigins);
    }

    @Override
    protected void configure() {
        install(new ConfigModule());
        install(new WebServicesModule());
        install(new ControllerModule());
        bind(AppStartup.class).in(Singleton.class);
    }
}
