package edu.pucmm.eict.bootstrap;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DatabaseBootstrap {
    private static DatabaseBootstrap instance;
    private static final Logger log = LoggerFactory.getLogger(DatabaseBootstrap.class);
    private static Server dbServer;

    private DatabaseBootstrap() {
    }

    public static synchronized DatabaseBootstrap getInstance() {
        if (instance == null) {
            instance = new DatabaseBootstrap();
        }
        return instance;
    }

    public void init(int port) {
        try {
            dbServer = Server.createTcpServer("-tcpPort", String.valueOf(port), "-tcpAllowOthers", "-ifNotExists").start();
            log.info("Current H2 Server Status: {}", dbServer.getStatus());
        } catch (SQLException ex) {
            log.error("Error occurred while initializing H2 in server mode: " + ex.getMessage());
        }
    }

    public void stop() {
        if(dbServer != null) {
            dbServer.stop();
            log.info("H2 server stopped. Current H2 Server Status: {}", dbServer.getStatus());
        }
    }
}