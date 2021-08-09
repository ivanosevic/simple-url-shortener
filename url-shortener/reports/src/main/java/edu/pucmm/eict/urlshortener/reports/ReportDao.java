package edu.pucmm.eict.urlshortener.reports;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ReportDao {

    private final EntityManagerFactory entityManagerFactory;

    public ReportDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
