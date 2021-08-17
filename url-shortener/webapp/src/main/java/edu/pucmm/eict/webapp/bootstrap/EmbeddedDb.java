package edu.pucmm.eict.webapp.bootstrap;

public interface EmbeddedDb {
    void start();
    void stop();
    String getStatus();
    String getUrl();
}

