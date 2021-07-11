package edu.pucmm.eict.reports;

import edu.pucmm.eict.common.MyEmf;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.List;

public class ReportDao {

    private static ReportDao instance;
    private final EntityManagerFactory emf;

    private ReportDao() {
        this.emf = MyEmf.getInstance().getEmf();
    }

    public static ReportDao getInstance() {
        if (instance == null) {
            instance = new ReportDao();
        }
        return instance;
    }

    public List<StringGroupByNum> URLsGroupByCountry(Long shortUrlId) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Tuple> result = em.createQuery("SELECT r.country, COUNT(*) FROM Referrer r " +
                    "JOIN r.shortURL as shortURL " +
                    "WHERE shortURL.id = :shortUrlId and shortURL.active = :active " +
                    "GROUP BY r.country", Tuple.class)
                    .setParameter("shortUrlId", shortUrlId)
                    .setParameter("active", true)
                    .getResultList();
            return StringGroupByNum.convertFromTuple(result);
        } finally {
            em.close();
        }
    }

    public List<StringGroupByNum> URLGroupByPlatform(Long shortUrlId) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Tuple> result = em.createQuery("SELECT r.platform, COUNT(*) FROM Referrer r " +
                    "JOIN r.shortURL as shortURL " +
                    "WHERE shortURL.id = :shortUrlId and shortURL.active = :active " +
                    "GROUP BY r.platform", Tuple.class)
                    .setParameter("shortUrlId", shortUrlId)
                    .setParameter("active", true)
                    .getResultList();
            return StringGroupByNum.convertFromTuple(result);
        } finally {
            em.close();
        }
    }

    public List<StringGroupByNum> URLGroupByBrowser(Long shortUrlId) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Tuple> result = em.createQuery("SELECT r.browser, COUNT(*) FROM Referrer r " +
                    "JOIN r.shortURL as shortURL " +
                    "WHERE shortURL.id = :shortUrlId and shortURL.active = :active " +
                    "GROUP BY r.browser", Tuple.class)
                    .setParameter("shortUrlId", shortUrlId)
                    .setParameter("active", true)
                    .getResultList();
            return StringGroupByNum.convertFromTuple(result);
        } finally {
            em.close();
        }
    }

    public List<StringGroupByNum> URLGroupByOS(Long shortUrlId) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Tuple> result = em.createQuery("SELECT r.operatingSystem, COUNT(*) FROM Referrer r " +
                    "JOIN r.shortURL as shortURL " +
                    "WHERE shortURL.id = :shortUrlId and shortURL.active = :active " +
                    "GROUP BY r.operatingSystem", Tuple.class)
                    .setParameter("shortUrlId", shortUrlId)
                    .setParameter("active", true)
                    .getResultList();
            return StringGroupByNum.convertFromTuple(result);
        } finally {
            em.close();
        }
    }

    public Long amountClicks(Long shortUrlId) {
        EntityManager em = emf.createEntityManager();
        try {
            String query = "SELECT COUNT(*) FROM Referrer r " +
                    "JOIN r.shortURL as shortURL " +
                    "WHERE shortURL.active = :active AND shortURL.id = :shortUrlId";
            return em.createQuery(query, Long.class)
                    .setParameter("shortUrlId", shortUrlId)
                    .setParameter("active", true)
                    .getResultList()
                    .stream().findFirst().get();
        } finally {
            em.close();
        }
    }

    public Long amountUniqueClicks(Long shortUrlId) {
        EntityManager em = emf.createEntityManager();
        try {
            String query = "SELECT COUNT( DISTINCT r.accessIp ) FROM Referrer r " +
                    "JOIN r.shortURL as shortURL " +
                    "WHERE shortURL.active = :active AND shortURL.id = :shortUrlId";
            return em.createQuery(query, Long.class)
                    .setParameter("shortUrlId", shortUrlId)
                    .setParameter("active", true)
                    .getResultList()
                    .stream().findFirst().get();
        } finally {
            em.close();
        }
    }

    public String topCountryByClicks(Long shortUrlId) {
        EntityManager em = emf.createEntityManager();
        try {
            String query = "SELECT r.country FROM Referrer r " +
                    "JOIN r.shortURL as shortURL " +
                    "WHERE shortURL.active = :active AND shortURL.id = :shortUrlId " +
                    "GROUP BY r.country " +
                    "ORDER BY COUNT(*) DESC";
            var result = em.createQuery(query, String.class)
                    .setParameter("shortUrlId", shortUrlId)
                    .setParameter("active", true)
                    .getResultList()
                    .stream().findFirst();
            if(result.isEmpty()) {
                return "-";
            } else {
                return result.get();
            }
        } finally {
            em.close();
        }
    }

    public Long amountClicksDuringLastDay(Long shortUrlId) {
        EntityManager em = emf.createEntityManager();
        try {
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusDays(1);
            String query = "SELECT COUNT(*) FROM Referrer r " +
                    "JOIN r.shortURL as shortURL " +
                    "WHERE shortURL.active = :active AND shortURL.id = :shortUrlId AND " +
                    "shortURL.createdAt BETWEEN :startDate AND :endDate";
            return em.createQuery(query, Long.class)
                    .setParameter("shortUrlId", shortUrlId)
                    .setParameter("active", true)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList()
                    .stream().findFirst().get();
        } finally {
            em.close();
        }
    }
}
