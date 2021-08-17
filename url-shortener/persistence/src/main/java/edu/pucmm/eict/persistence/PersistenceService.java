package edu.pucmm.eict.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceService {
    private EntityManagerFactory entityManagerFactory;
    private final String persistenceUnit;

    public PersistenceService(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

    public void start() {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
    }

    public void stop() {
        entityManagerFactory.close();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}