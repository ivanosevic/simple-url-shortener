package edu.pucmm.eict.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    private static ApplicationProperties instance;
    private static final Logger log = LoggerFactory.getLogger(ApplicationProperties.class);
    private final Properties properties;
    private final int port;
    private final String domain;
    private final boolean embedded;
    private final int h2Port;
    private final String dbUsername;
    private final String dbPassword;
    private final String dbUrl;
    private final String dbDialect;
    private final String dbDriver;
    private final String showSql;
    private final String generateDdl;
    private final String encryptPass;

    private ApplicationProperties() {
        properties = loadPropertiesFile();
        port = Integer.parseInt(properties.getProperty("app.port", "7000"));
        domain = properties.getProperty("app.domain");
        embedded = Boolean.parseBoolean(properties.getProperty("app.db.embedded", "false"));
        h2Port = Integer.parseInt(properties.getProperty("app.db.embedded.port", "9092"));
        dbUsername = properties.getProperty("app.db.username");
        dbPassword = properties.getProperty("app.db.password");
        dbUrl = properties.getProperty("app.db.url");
        dbDialect = properties.getProperty("app.db.dialect");
        dbDriver = properties.getProperty("app.db.driver");
        showSql = properties.getProperty("app.db.show-sql");
        generateDdl = properties.getProperty("app.db.generate-ddl");
        encryptPass = properties.getProperty("app.encrypt.pass");
    }

    public static ApplicationProperties getInstance() {
        if (instance == null) {
            instance = new ApplicationProperties();
        }
        return instance;
    }

    private Properties loadPropertiesFile() {
        Properties properties = new Properties();
        try {
            try(InputStream is = ClassLoader.getSystemResourceAsStream("application.properties")) {
                properties.load(is);
            }
            log.info("Successfully read application.properties");
        } catch (Exception ex) {
            log.error("Error while fetching app.properties: {}", ex.getMessage());
        }
        return properties;
    }

    public static Logger getLog() {
        return log;
    }

    public Properties getProperties() {
        return properties;
    }

    public int getPort() {
        return port;
    }

    public String getDomain() {
        return domain;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public int getH2Port() {
        return h2Port;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbDialect() {
        return dbDialect;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public String getShowSql() {
        return showSql;
    }

    public String getGenerateDdl() {
        return generateDdl;
    }

    public String getEncryptPass() {
        return encryptPass;
    }
}