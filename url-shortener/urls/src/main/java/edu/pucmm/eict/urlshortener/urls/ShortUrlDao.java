package edu.pucmm.eict.urlshortener.urls;

import edu.pucmm.eict.urlshortener.persistence.Page;
import edu.pucmm.eict.urlshortener.persistence.PaginationDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class ShortUrlDao extends PaginationDao<ShortUrl, Long> {

    public ShortUrlDao(EntityManagerFactory entityManagerFactory) {
        super(ShortUrl.class, entityManagerFactory);
    }

    public Optional<ShortUrl> findByCode(String code) {
        EntityManager em = super.getEntityManager();
        try {
            String query = "select su from ShortUrl su where su.active = true and su.code = :code";
            return em.createQuery(query, ShortUrl.class)
                    .setParameter("code", code)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Page<ShortUrl> findPaged(int page, int size) {
        EntityManager em = super.getEntityManager();
        try {
            TypedQuery<Long> count = em.createQuery("select s from ShortUrl s where s.active = true", Long.class);
            TypedQuery<Long> ids = em.createQuery("select s.id from ShortUrl s where s.active = true", Long.class);
            TypedQuery<ShortUrl> query = em.createQuery("select s from ShortUrl s join fetch s.user s.id in (:ids)", ShortUrl.class);
            return super.findPagedFilter(page, size, count, ids, query);
        } finally {
            em.close();
        }
    }
}
