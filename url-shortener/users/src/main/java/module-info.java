open module url.shortener.users {

    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.transaction;
    requires io.javalin;
    requires url.shortener.persistence;
    requires jasypt;
    exports edu.pucmm.eict.urlshortener.users;
}
