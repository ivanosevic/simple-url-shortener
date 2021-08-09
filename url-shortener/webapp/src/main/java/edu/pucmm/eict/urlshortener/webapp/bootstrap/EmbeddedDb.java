package edu.pucmm.eict.urlshortener.webapp.bootstrap;

public interface EmbeddedDb {
    void start();
    void stop();
    String getStatus();
    String getUrl();
}
