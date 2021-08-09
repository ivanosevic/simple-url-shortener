package edu.pucmm.eict.urlshortener.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Dao<T, PK extends Serializable> {

    private final EntityManagerFactory entityManagerFactory;
    protected Class<T> entityClass;

    public Dao(Class<T> entityClass, EntityManagerFactory entityManagerFactory) {
        this.entityClass = entityClass;
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
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

    public T update(T entity) throws PersistenceException {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
            return entity;
        } finally {
            em.close();
        }
    }

    public T delete(T entity) throws PersistenceException {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
            return entity;
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
