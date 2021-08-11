open module url.shortener.restapi {
    requires url.shortener.persistence;
    requires url.shortener.users;
    requires url.shortener.urls;
    requires url.shortener.reports;
    requires java.sql;
    requires java.persistence;
    requires java.transaction;
    requires com.h2database;
    requires io.javalin;
    requires org.slf4j;
    requires org.slf4j.simple;
    requires java.xml.bind;
    requires annotations;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    // Rest Client API
    requires javax.ws.rs.api;
    requires commons.validator;
    requires io.seruco.encoding.base62;
    // User Agent Analyzer
    requires yauaa;
    requires modelmapper;
    requires org.eclipse.jetty.http;
    requires javax.servlet.api;
    requires kotlin.stdlib;
    requires java.security.jgss;
    requires java.security.sasl;
    requires jasypt;
    requires jjwt;

    requires com.google.gson;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
}
