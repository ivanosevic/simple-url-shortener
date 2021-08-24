package edu.pucmm.eict.urls;

import edu.pucmm.eict.persistence.Dao;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

public class ClickDao extends Dao<Click, Long> {

    @Inject
    public ClickDao(EntityManagerFactory entityManagerFactory) {
        super(Click.class, entityManagerFactory);
    }
}

