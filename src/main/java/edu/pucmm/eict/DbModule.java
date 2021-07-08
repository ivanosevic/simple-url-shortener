package edu.pucmm.eict;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import edu.pucmm.eict.common.AppProperties;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class DbModule extends AbstractModule {

    @Provides
    @Singleton
    public EntityManagerFactory getEmf(AppProperties appProperties) {
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

    @Override
    protected void configure() {
        super.configure();
    }
}
