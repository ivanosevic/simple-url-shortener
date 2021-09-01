package edu.pucmm.eict.grpc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import edu.pucmm.eict.grpc.config.ConfigModule;
import edu.pucmm.eict.grpc.dto.DtoModule;
import edu.pucmm.eict.grpc.services.ExceptionHandler;
import edu.pucmm.eict.grpc.services.ServicesModule;
import edu.pucmm.eict.grpc.services.ShortUrlServicesGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

public class AppModule extends AbstractModule {

    @Provides
    @Singleton
    public Server getServer(@Named("serverPort") Integer port, ExceptionHandler exceptionHandler, ShortUrlServicesGrpc shortUrlServicesGrpc) {
        Server server = ServerBuilder.forPort(port)
                .intercept(exceptionHandler)
                .addService(shortUrlServicesGrpc)
                .build();
        return server;
    }

    @Override
    protected void configure() {
        install(new ConfigModule());
        install(new DtoModule());
        install(new ServicesModule());
    }
}
