package edu.pucmm.eict.urlshortener.reports;

import edu.pucmm.eict.urlshortener.reports.ReportDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.List;

public class UrlStatisticsDao extends ReportDao {

    public UrlStatisticsDao(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public Long getClicks(Long shortUrlId) {
        EntityManager em = super.getEntityManager();
        try {
            String query = "select count(c) from Click c join c.shortUrl shortUrl where shortUrl.id = :id";
            return em.createQuery(query, Long.class).setParameter("id", shortUrlId)
                    .getResultStream().findFirst().orElse(0L);
        } finally {
            em.close();
        }
    }

    public Long getUniqueClicks(Long shortUrlId) {
        EntityManager em = super.getEntityManager();
        try {
            String query = "select count(distinct c.ip) from Click c join c.shortUrl shortUrl where shortUrl.id = :id";
            return em.createQuery(query, Long.class).setParameter("id", shortUrlId)
                    .getResultStream().findFirst().orElse(0L);
        } finally {
            em.close();
        }
    }

    public Long getClicksLast24Hours(Long shortUrlId) {
        EntityManager em = super.getEntityManager();
        try {
            LocalDateTime end = LocalDateTime.now();
            LocalDateTime start = end.minusDays(1);
            String query = "select count(c) from Click c join c.shortUrl shortUrl " +
                    "where shortUrl.id = :id and shortUrl.active = true and " +
                    "shortUrl.createdAt between :start and :end";
            return em.createQuery(query, Long.class)
                    .setParameter("id", shortUrlId)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultStream().findFirst().orElse(0L);
        } finally {
            em.close();
        }
    }

    public List<Tuple> groupedByPlatform(Long shortUrlId) {
        EntityManager em = super.getEntityManager();
        try {
            String query = "select c.platform, count(*) from Click c " +
                    "join c.shortUrl as shortUrl " +
                    "where shortUrl.id = :id and shortUrl.active = true " +
                    "group by c.platform";
             return em.createQuery(query, Tuple.class)
                    .setParameter("id", shortUrlId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Tuple> groupedByOs(Long shortUrlId) {
        EntityManager em = super.getEntityManager();
        try {
            String query = "select c.os, count(*) from Click c " +
                    "join c.shortUrl as shortUrl " +
                    "where shortUrl.id = :id and shortUrl.active = true " +
                    "group by c.os";
            return em.createQuery(query, Tuple.class)
                    .setParameter("id", shortUrlId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Tuple> groupedByBrowser(Long shortUrlId) {
        EntityManager em = super.getEntityManager();
        try {
            String query = "select c.browser, count(*) from Click c " +
                    "join c.shortUrl as shortUrl " +
                    "where shortUrl.id = :id and shortUrl.active = true " +
                    "group by c.browser";
            return em.createQuery(query, Tuple.class)
                    .setParameter("id", shortUrlId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Tuple> clicksByCountry(Long shortUrlId) {
        EntityManager em = super.getEntityManager();
        try {
            String query = "select c.countryIso2, count(*) from Click c " +
                    "join c.shortUrl as shortUrl " +
                    "where shortUrl.id = :id and shortUrl.active = true " +
                    "group by c.countryIso2";
            return em.createQuery(query, Tuple.class)
                    .setParameter("id", shortUrlId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
