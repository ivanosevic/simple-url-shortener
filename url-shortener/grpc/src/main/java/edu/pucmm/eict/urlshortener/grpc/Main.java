package edu.pucmm.eict.urlshortener.grpc;

import edu.pucmm.eict.urlshortener.persistence.PersistenceService;
import edu.pucmm.eict.urlshortener.reports.*;
import edu.pucmm.eict.urlshortener.urls.*;
import edu.pucmm.eict.urlshortener.users.UserDao;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.seruco.encoding.base62.Base62;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final int SERVER_PORT = 7002;
    private static final String PERSISTENCE_UNIT = "embedded";
    private static final String DOMAIN = "wolfisc.com";

    public static void main(String[] args) throws IOException, InterruptedException {

        /**
         * Initializing database connection through Entity Manager Factory..
         */
        log.info("Initializing persistence services");
        PersistenceService persistenceService = new PersistenceService(PERSISTENCE_UNIT);
        persistenceService.start();

        EntityManagerFactory entityManagerFactory = persistenceService.getEntityManagerFactory();

        /**
         * Initializing DAOs and Services to use
         * with the gRPC Server.
         */
        log.info("Creating DAOs and Services to use");
        UserDao userDao = new UserDao(entityManagerFactory);

        Base62 base62Encoder = Base62.createInstance();
        org.apache.commons.validator.routines.UrlValidator apacheUrlValidator = new org.apache.commons.validator.routines.UrlValidator();
        UrlValidator urlValidator = new UrlValidator(apacheUrlValidator);
        RedirectUrlBuilder redirectUrlBuilder = new BasicRedirectUrlBuilder(DOMAIN);

        ShortUrlDao shortUrlDao = new ShortUrlDao(entityManagerFactory);
        ShortUrlService shortUrlService = new ShortUrlService(shortUrlDao, base62Encoder, urlValidator);

        DataConverter dataConverter = new DataConverter();
        UrlStatisticsDao urlStatisticsDao = new UrlStatisticsDao(entityManagerFactory);
        UrlStatisticsService urlStatisticsService = new UrlStatisticsService(urlStatisticsDao, dataConverter);

        ModelMapper modelMapper = new ModelMapper();
        ShortUrlDtoConverter shortUrlDtoConverter = new ShortUrlDtoConverter(redirectUrlBuilder);
        modelMapper.addConverter(shortUrlDtoConverter);

        /**
         * Creating gRPC server
         */
        log.info("Creating gRPC server on port " + SERVER_PORT);
        Server server = ServerBuilder.forPort(SERVER_PORT)
                .intercept(new ExceptionHandler())
                .addService(new ShortUrlServicesGrpc(urlStatisticsService, shortUrlService, modelMapper, shortUrlDao, userDao))
                .build();
        server.start();
        server.awaitTermination();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Shutting down application from JVM.");
            persistenceService.stop();
            server.shutdown();
        }));
    }
}
