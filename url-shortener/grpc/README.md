# gRPC Server - Simple Url Shortener

## About

## Before running the gRPC Server

It's important that the web application is running first, because the database is embedded
to the server side application. You can build the web application if you go to the
root of the project, and execute the following command:

```sh 
./gradlew clean build
```

The executable jar will be located on the grpc module, inside a folder called 'build/libs'. Now go to that location
and write the following command in your terminal:
```sh 
java -jar webapp-1.0.jar
```

This will execute the web application on its specified port. The default port is 7000.


## Building and running the gRPC Server

If you executed the first command in the section 'Before running the gRPC Server', the executable
jar should be located in the grpc module inside the build/libs folder. Go there and execute the jar as follows:

```sh
java -jar grpc-1.0.jar
```

The default port of the gRPC server is 7003.

