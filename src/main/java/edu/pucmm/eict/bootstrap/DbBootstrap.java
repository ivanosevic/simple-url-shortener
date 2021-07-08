package edu.pucmm.eict.bootstrap;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.sql.SQLException;

@Singleton
public class DbBootstrap {
    private static final Logger logger = LoggerFactory.getLogger(DbBootstrap.class);

    private static Server dbServer;

    public void init(int port) {
        try {
            dbServer = Server.createTcpServer("-tcpPort", String.valueOf(port), "-tcpAllowOthers", "-ifNotExists").start();
            logger.info("Current H2 Server Status: {}", dbServer.getStatus());
        } catch (SQLException ex) {
            logger.error("Error occurred while initializing H2 in server mode: " + ex.getMessage());
        }
    }

    public void stop() {
        if(dbServer != null) {
            dbServer.stop();
            logger.info("H2 server stopped. Current H2 Server Status: {}", dbServer.getStatus());
        }
    }
}