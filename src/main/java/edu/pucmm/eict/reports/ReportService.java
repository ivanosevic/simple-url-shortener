package edu.pucmm.eict.reports;

import edu.pucmm.eict.common.MyEmf;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportService {

    private static ReportService instance;
    private final EntityManagerFactory emf;

    private ReportService() {
        this.emf = MyEmf.getInstance().getEmf();
    }

    public static ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }

    private List<URLGroupByCountry> tupleToURLGroupByCountry(List<Tuple> result) {
        List<URLGroupByCountry> data = new ArrayList<>();
        for(var t : result) {
            String country = (String) t.get(0);
            Long quantity = (Long) t.get(1);
            data.add(new URLGroupByCountry(country, quantity));
        }
        return data;
    }

    public List<URLGroupByCountry> URLsGroupByCountry(Long shortUrlId) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Tuple> result = em.createQuery("SELECT r.country, COUNT(*) FROM Referrer r " +
                    "JOIN r.shortURL as shortURL " +
                    "WHERE shortURL.id = :shortUrlId and shortURL.active = :active " +
                    "GROUP BY r.country", Tuple.class)
                    .setParameter("shortUrlId", shortUrlId)
                    .setParameter("active", true)
                    .getResultList();
            return tupleToURLGroupByCountry(result);
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