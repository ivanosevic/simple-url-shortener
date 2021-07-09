package edu.pucmm.eict.urls;

import edu.pucmm.eict.common.Dao;

import java.util.Optional;

public class ShortenedUrlDao extends Dao<ShortenedUrl, Long> {

    private static ShortenedUrlDao instance;

    private ShortenedUrlDao() {
        super(ShortenedUrl.class);
    }

    public static ShortenedUrlDao getInstance() {
        if (instance == null) {
            instance = new ShortenedUrlDao();
        }
        return instance;
    }

    public Optional<ShortenedUrl> findByCode(String code) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM ShortenedUrl s WHERE s.code = :code AND s.active = :active", ShortenedUrl.class)
                    .setParameter("code", code)
                    .setParameter("active", true)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }
}