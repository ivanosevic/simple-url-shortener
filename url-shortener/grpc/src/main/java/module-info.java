module grpc {
    requires url.shortener.persistence;
    requires url.shortener.users;
    requires url.shortener.urls;
    requires url.shortener.reports;

    requires java.sql;
    requires java.persistence;
    requires java.transaction;
    requires com.h2database;

    requires org.slf4j;
    requires org.slf4j.simple;

    requires com.google.zxing;
    requires com.google.zxing.javase;

    requires javax.ws.rs.api;
    requires commons.validator;
    requires io.seruco.encoding.base62;

    requires modelmapper;

    requires grpc.protobuf;
    requires grpc.api;
    requires grpc.core;
    requires grpc.stub;
    requires com.google.protobuf;
    requires javax.annotation.api;
    requires com.google.common;
}
