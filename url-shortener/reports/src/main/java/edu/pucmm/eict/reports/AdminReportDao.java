package edu.pucmm.eict.reports;

import edu.pucmm.eict.persistence.GenericDao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Optional;

public class AdminReportDao extends GenericDao {

    @Inject
    public AdminReportDao(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public Optional<Long> totalUsers() {
        EntityManager em = super.getEntityManager();
        try {
            String query = "select count(u) from User u where u.deletedAt = 0L";
            return em.createQuery(query, Long.class)
                    .getResultList()
                    .stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<Long> totalLinks() {
        EntityManager em = super.getEntityManager();
        try {
            String query = "select count(s) from ShortUrl s";
            return em.createQuery(query, Long.class)
                    .getResultList()
                    .stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<Long> totalClicks() {
        EntityManager em = super.getEntityManager();
        try {
            String query = "select count(c) from Click c";
            return em.createQuery(query, Long.class)
                    .getResultList()
                    .stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<Long> totalLinksLastDay() {
        EntityManager em = super.getEntityManager();
        try {
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusDays(1);
            String query = "select count(s) FROM ShortUrl s " +
                    "where s.createdAt between :startDate and :endDate";
            return em.createQuery(query, Long.class)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList()
                    .stream().findFirst();
        } finally {
            em.close();
        }
    }
}
