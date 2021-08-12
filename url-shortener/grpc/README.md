# gRPC Server - Simple Url Shortener

## About

## Before running the gRPC Server

It's important that the web application is running first, because the database is embedded
to the server side application. You can build the web application if you go to the 
root of the project, and execute the following command:

```sh 
mvn clean install 
```

The executable jar will be located on the grpc module, inside a folder called 'target'. Now go to that location
and write the following command in your terminal:
```sh 
java -jar webapp-1.0-SNAPSHOT.jar
```

This will execute the web application on its specified port. The default port is 7000.


## Building and running the gRPC Server

If you executed the first command in the section 'Before running the gRPC Server', the executable 
jar should be located in the grpc module inside the target folder. Go there and execute the jar as follows:

```sh
java -jar grpc-1.0-SNAPSHOT.jar
```

The default port of the gRPC server is 7002.

## Configuration
Inside the resource folder of the gRPC module there's an application.properties file. This file
has the basic configuration of the gRPC server. It contains the following properties:
- app.port: The port which the server will run on. The default port is 7002.
- app.db.persistence-unit: This will tell the application which persistence unit the Entity Manager Factory
  will use.
- app.redirect.url: This will be the domain which the app will use as a base to construct the shortened url.
This value must match with the app.redirect.url that the web application has.
  
