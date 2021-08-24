package edu.pucmm.eict.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import edu.pucmm.eict.restapi.config.ConfigModule;
import edu.pucmm.eict.restapi.config.SecurityConfig;
import edu.pucmm.eict.restapi.dtos.DtoModule;
import edu.pucmm.eict.restapi.endpoints.EndpointModule;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.RedocOptionsObject;
import io.javalin.plugin.openapi.ui.RedocOptionsTheme;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import javax.inject.Singleton;

public class AppModule extends AbstractModule {

    @Provides
    @Singleton
    public OpenApiOptions getOpenApiConfig() {
        return new OpenApiOptions(new Info().version("1.0").description("Simple Url Shortener API"))
                .activateAnnotationScanningFor("edu.pucmm.eict.restapi")
                .path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger").title("Simple Url Shortener API Documentation"))
                .reDoc(new ReDocOptions("/redoc", new RedocOptionsObject.Builder()
                        .setHideDownloadButton(true)
                        .setTheme(
                                new RedocOptionsTheme.Builder()
                                        .setSpacingUnit(10)
                                        .setTypographyOptimizeSpeed(true)
                                        .build()
                        ).build()
                ).title("Simple Url Shortener RestAPI"));
    }

    @Provides
    @Singleton
    public Javalin getJavalinInstance(OpenApiOptions openApiOptions, ObjectMapper objectMapper,
                                      SecurityConfig securityConfig) {
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new OpenApiPlugin(openApiOptions));
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
            config.accessManager(securityConfig);
            config.showJavalinBanner = false;
        });
        JavalinJackson.configure(objectMapper);
        return app;
    }

    @Override
    protected void configure() {
        install(new DtoModule());
        install(new ConfigModule());
        install(new EndpointModule());
        bind(AppStartup.class).in(Singleton.class);
    }
}
