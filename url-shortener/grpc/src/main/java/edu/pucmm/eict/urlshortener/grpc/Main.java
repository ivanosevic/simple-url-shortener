package edu.pucmm.eict.urlshortener.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Main {
    private static final int SERVER_PORT = 7002;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(SERVER_PORT).build().start();
        server.awaitTermination();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.shutdown();
            System.out.println("Server down...");
        }));
    }
}
