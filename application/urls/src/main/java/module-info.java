module url.shortener.urls {
    // ORM
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.transaction;
    // QR Generator
    requires com.google.zxing;
    requires com.google.zxing.javase;
    // Rest Client API
    requires javax.ws.rs.api;
    requires commons.validator;
    requires io.seruco.encoding.base62;
    // User Agent Analyzer
    requires yauaa;
    // Other libraries
    requires url.shortener.persistence;
    requires url.shortener.users;
    requires java.desktop;


    exports edu.pucmm.eict.urlshortener.urls;
}