package edu.pucmm.eict.urlshortener.urls;

import edu.pucmm.eict.urlshortener.persistence.Dao;

import javax.persistence.EntityManagerFactory;

public class ClickDao extends Dao<Click, Long> {

    public ClickDao(EntityManagerFactory entityManagerFactory) {
        super(Click.class, entityManagerFactory);
    }
}
