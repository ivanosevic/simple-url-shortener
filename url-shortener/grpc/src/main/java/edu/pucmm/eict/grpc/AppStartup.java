package edu.pucmm.eict.grpc;

import io.grpc.Server;

import javax.inject.Inject;
import java.io.IOException;

public class AppStartup implements Startup {

    private final Server server;

    @Inject
    public AppStartup(Server server) {
        this.server = server;
    }

    @Override
    public void start(String[] args) {
        try {
            server.start();
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
