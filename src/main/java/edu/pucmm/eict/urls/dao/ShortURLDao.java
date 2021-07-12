package edu.pucmm.eict.urls.dao;

import edu.pucmm.eict.common.Dao;
import edu.pucmm.eict.common.Page;
import edu.pucmm.eict.urls.models.ShortURL;
import org.hibernate.jpa.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class ShortURLDao extends Dao<ShortURL, Long> {

    private static ShortURLDao instance;

    private ShortURLDao() {
        super(ShortURL.class);
    }

    public Page<ShortURL> findPaged(Integer page, Integer size) {
        EntityManager em = this.getEntityManager();
        try {
            TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(s) FROM ShortURL s WHERE s.active = :active", Long.class)
                    .setParameter("active", true);
            TypedQuery<Long> ids = em.createQuery("SELECT s.id FROM ShortURL s LEFT JOIN s.user AS user WHERE s.active = :active GROUP BY s.id ORDER BY MAX(s.createdAt) DESC", Long.class)
                    .setParameter("active", true);
            TypedQuery<ShortURL> query = em.createQuery("SELECT DISTINCT s FROM ShortURL s LEFT JOIN FETCH s.user AS user WHERE s.id IN (:ids) GROUP BY s.id ORDER BY MAX(s.createdAt) DESC", ShortURL.class)
                    .setHint(
                            QueryHints.HINT_PASS_DISTINCT_THROUGH,
                            false
                    );
            return super.findPagedFilter(page, size, countQuery, ids, query);
        } finally {
            em.close();
        }
    }

    public static ShortURLDao getInstance() {
        if (instance == null) {
            instance = new ShortURLDao();
        }
        return instance;
    }

    public Optional<ShortURL> findByCode(String code) {
        EntityManager em = this.getEntityManager();
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
        EntityManager em = this.getEntityManager();
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