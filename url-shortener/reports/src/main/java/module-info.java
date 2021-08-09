module url.shortener.reports {
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires url.shortener.persistence;
    requires url.shortener.urls;
    exports edu.pucmm.eict.urlshortener.reports;
}