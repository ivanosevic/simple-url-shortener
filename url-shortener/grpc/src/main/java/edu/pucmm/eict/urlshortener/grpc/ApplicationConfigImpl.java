package edu.pucmm.eict.urlshortener.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class ApplicationConfigImpl implements ApplicationConfig {

    private static final Logger log = LoggerFactory.getLogger(ApplicationConfigImpl.class);
    private final String file;
    private Properties properties;

    public ApplicationConfigImpl(String file) {
        this.file = file;
    }

    private Properties LoadPropertiesFile() {
        Properties properties = new Properties();
        log.info("Loading {}.", file);
        try {
            try(InputStream is = ApplicationConfigImpl.class.getResourceAsStream(file)) {
                properties.load(is);
            }
            log.info("Successfully read {}", file);
        } catch (Exception ex) {
            log.error("Error loading:  {} | Reason: {}", file, ex.getMessage());
        }
        return properties;
    }

    @Override
    public void load() {
        this.properties = LoadPropertiesFile();
    }

    @Override
    public String getPropertyAsString(String property) {
        return String.valueOf(properties.getProperty(property));
    }

    @Override
    public int getPropertyAsInt(String property) {
        return Integer.parseInt(properties.getProperty(property));
    }
}
