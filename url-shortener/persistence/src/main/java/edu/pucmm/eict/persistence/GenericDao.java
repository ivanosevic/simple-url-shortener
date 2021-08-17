package edu.pucmm.eict.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class GenericDao {
    private final EntityManagerFactory entityManagerFactory;

    public GenericDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
