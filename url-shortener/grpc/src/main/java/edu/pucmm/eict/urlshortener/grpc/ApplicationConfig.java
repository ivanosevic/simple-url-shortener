package edu.pucmm.eict.urlshortener.grpc;

public interface ApplicationConfig {
    void load();
    String getPropertyAsString(String property);
    int getPropertyAsInt(String property);
}
