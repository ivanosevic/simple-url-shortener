open module url.shortener.users {

    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.transaction;
    requires io.javalin;
    requires jasypt;
    requires url.shortener.persistence;

    exports edu.pucmm.eict.urlshortener.users;
}
