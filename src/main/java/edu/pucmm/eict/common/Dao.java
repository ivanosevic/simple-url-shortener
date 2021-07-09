package edu.pucmm.eict.common;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Dao<T, PK extends Serializable> {

    private final ApplicationProperties appProperties;
    private static EntityManagerFactory emf;
    protected Class<T> entityClass;

    public Dao(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.appProperties = ApplicationProperties.getInstance();
        if(emf == null) {
            emf = buildEmf();
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private EntityManagerFactory buildEmf() {
        String driver = appProperties.getDbDriver();
        String url = appProperties.getDbUrl();
        String user = appProperties.getDbUsername();
        String password = appProperties.getDbPassword();
        String showSql = appProperties.getShowSql();
        String dialect = appProperties.getDbDialect();
        String ddl = appProperties.getGenerateDdl();
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", dialect);
        properties.put("javax.persistence.jdbc.driver", driver);
        properties.put("javax.persistence.jdbc.url", url);
        properties.put("javax.persistence.jdbc.user", user);
        properties.put("javax.persistence.jdbc.password", password);
        properties.put("hibernate.show_sql", showSql);
        properties.put("hibernate.hbm2ddl.auto", ddl);
        return Persistence.createEntityManagerFactory("main-unit", properties);
    }

    public T create(T entity) {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } finally {
            em.close();
        }
    }

    public T update(T entidad) throws PersistenceException {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entidad);
            em.getTransaction().commit();
            return entidad;
        } finally {
            em.close();
        }
    }

    public T delete(T entidad) throws PersistenceException {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(entidad);
            em.getTransaction().commit();
            return entidad;
        } finally {
            em.close();
        }
    }

    public Optional<T> findById(PK id) throws PersistenceException {
        EntityManager em = this.getEntityManager();
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } finally {
            em.close();
        }
    }

    public List<T> findAll() throws PersistenceException {
        EntityManager em = this.getEntityManager();
        try {
            CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(entityClass);
            query.select(query.from(entityClass));
            return em.createQuery(query).getResultList();
        } finally {
            em.close();
        }
    }
}