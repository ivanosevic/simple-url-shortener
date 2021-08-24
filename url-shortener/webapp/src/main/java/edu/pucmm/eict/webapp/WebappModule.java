package edu.pucmm.eict.webapp;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import edu.pucmm.eict.webapp.bootstrap.BootstrapModule;
import edu.pucmm.eict.webapp.configuration.ConfigurationModule;
import edu.pucmm.eict.webapp.configuration.CustomThymeleafRenderer;
import edu.pucmm.eict.webapp.configuration.SecurityConfig;
import edu.pucmm.eict.webapp.controllers.ControllerModule;
import edu.pucmm.eict.webapp.converter.ConverterModule;
import edu.pucmm.eict.webapp.sessionurls.SessionUrlModule;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;

import javax.inject.Named;
import javax.inject.Singleton;

public class WebappModule extends AbstractModule  {

    @Provides
    @Singleton
    @Named("passwordEncryptor")
    public String getPasswordEncryptor() {
        return "wd583szUNc8bNZSA";
    }

    @Provides
    @Singleton
    @Named("domain")
    public String getDomain() {
        return "short.wolfisc.com";
    }

    @Provides
    @Singleton
    @Named("persistenceUnit")
    public String getPersistenceUnit() {
        return "embedded";
    }

    @Provides
    @Singleton
    @Named("h2Port")
    public int getH2Port() {
        return 9092;
    }

    @Provides
    @Singleton
    public Javalin getJavalinInstance(CustomThymeleafRenderer customThymeleafRenderer, SecurityConfig securityConfig) {
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
            config.showJavalinBanner = false;
            config.enableCorsForAllOrigins();
            config.addStaticFiles("/public", "/public", Location.CLASSPATH);
        });
        JavalinRenderer.register(customThymeleafRenderer, ".html");
        app.config.accessManager(securityConfig);
        return app;
    }

    @Override
    protected void configure() {
        install(new BootstrapModule());
        install(new ConverterModule());
        install(new ConfigurationModule());
        install(new ControllerModule());
        install(new SessionUrlModule());
    }
}
