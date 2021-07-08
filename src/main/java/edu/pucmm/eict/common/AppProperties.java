package edu.pucmm.eict.common;

import com.google.inject.Singleton;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

@Getter
@Singleton
public class AppProperties {

    private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);
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

    public AppProperties() {
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
    }

    private Properties loadPropertiesFile() {
        Properties properties = new Properties();
        try {
            try(InputStream is = ClassLoader.getSystemResourceAsStream("application.properties")) {
                properties.load(is);
            }
            logger.info("Successfully read application.properties");
        } catch (Exception ex) {
            logger.error("Error while fetching app.properties: {}", ex.getMessage());
        }
        return properties;
    }
}