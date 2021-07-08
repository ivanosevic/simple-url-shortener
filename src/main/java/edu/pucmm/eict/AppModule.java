package edu.pucmm.eict;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.javalin.Javalin;

public class AppModule extends AbstractModule {

    @Provides
    @Singleton
    public Javalin getJavalin() {
        return Javalin.create(javalinConfig -> {
            javalinConfig.addStaticFiles("/public");
            javalinConfig.enableCorsForAllOrigins();
        });
    }

    @Override
    protected void configure() {
        super.configure();
    }
}