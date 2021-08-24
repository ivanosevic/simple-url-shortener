package edu.pucmm.eict.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceModule extends AbstractModule {

    @Provides
    @Singleton
    public EntityManagerFactory getEntityManagerFactory(@Named("persistenceUnit") String persistenceUnit) {
        return Persistence.createEntityManagerFactory(persistenceUnit);
    }

    @Override
    protected void configure() {
        super.configure();
    }
}
