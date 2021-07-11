package edu.pucmm.eict.urls.dao;

import edu.pucmm.eict.common.Dao;
import edu.pucmm.eict.urls.models.ShortURL;

import java.util.Optional;

public class ShortURLDao extends Dao<ShortURL, Long> {

    private static ShortURLDao instance;

    private ShortURLDao() {
        super(ShortURL.class);
    }

    public static ShortURLDao getInstance() {
        if (instance == null) {
            instance = new ShortURLDao();
        }
        return instance;
    }

    public Optional<ShortURL> findByCode(String code) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM ShortURL s WHERE s.code = :code AND s.active = :active", ShortURL.class)
                    .setParameter("code", code)
                    .setParameter("active", true)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<ShortURL> findById(Long id) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM ShortURL s WHERE s.id = :id AND s.active = :active", ShortURL.class)
                    .setParameter("id", id)
                    .setParameter("active", true)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }
}