# SOAP Web Services - Simple Url Shortener

## About

## Before running the SOAP Server

It's important that the web application is running first, because the database is embedded
to the server side application. You can build the web application if you go to the
root of the project, and execute the following command:

```sh 
mvn clean install 
```

The executable jar will be located on the grpc module, inside a folder called 'build/libs'. Now go to that location
and write the following command in your terminal:
```sh 
java -jar webapp-1.0.jar
```

This will execute the web application on its specified port. The default port is 7000.


## Building and running the SOAP Server

If you executed the first command in the section 'Before running the SOAP Server', the executable
jar should be located in the grpc module inside the build/libs folder. Go there and execute the jar as follows:

```sh
java -jar soap-1.0.jar
```

The default port of the RestAPI server is 7002.

## SOAP WSDL

There is only one web service, and its route is:
- host:port/ws/ShortUrlWebServices?wsdl