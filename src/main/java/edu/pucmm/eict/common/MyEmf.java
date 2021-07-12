package edu.pucmm.eict.common;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class MyEmf {

    private static MyEmf myEmfInstance;
    private EntityManagerFactory emf;
    private ApplicationProperties appProperties;

    private MyEmf() {
        this.appProperties = ApplicationProperties.getInstance();
        this.emf = this.buildEmf();
    }

    public static MyEmf getInstance() {
        if (myEmfInstance == null) {
            myEmfInstance = new MyEmf();
        }
        return myEmfInstance;
    }

    private EntityManagerFactory buildEmf() {
        String driver = appProperties.getDbDriver();
        String url = appProperties.getDbUrl();
        String user = appProperties.getDbUsername();
        String password = appProperties.getDbPassword();
        String showSql = appProperties.getShowSql();
        String dialect = appProperties.getDbDialect();
        String ddl = appProperties.getGenerateDdl();
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", dialect);
        properties.put("javax.persistence.jdbc.driver", driver);
        properties.put("javax.persistence.jdbc.url", url);
        properties.put("javax.persistence.jdbc.user", user);
        properties.put("javax.persistence.jdbc.password", password);
        properties.put("hibernate.show_sql", showSql);
        properties.put("hibernate.hbm2ddl.auto", ddl);
        return Persistence.createEntityManagerFactory("main-unit", properties);
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }
}
