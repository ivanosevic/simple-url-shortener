package edu.pucmm.eict.urlshortener.webapp.bootstrap;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class H2Database implements EmbeddedDb {
    private static final Logger log = LoggerFactory.getLogger(H2Database.class);
    private final int port;
    private Server server;

    public H2Database(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        try {
            server = Server.createTcpServer("-tcpPort", String.valueOf(port), "-tcpAllowOthers", "-ifNotExists");
            log.info("H2 database created: {}", server.getStatus());
            server.start();
            log.info("Running H2 database: {}", server.getStatus());
        } catch (SQLException e) {
            log.error("Error starting H2 Database: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if(server != null) {
            server.stop();
            log.info("Stopping H2 database server: {}", server.getStatus());
        }
    }

    @Override
    public String getStatus() {
        return server.getStatus();
    }

    @Override
    public String getUrl() {
        return server.getURL();
    }
}
