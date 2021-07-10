package edu.pucmm.eict.common;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Dao<T, PK extends Serializable> {

    private final MyEmf myEmf;
    private static EntityManagerFactory emf;
    protected Class<T> entityClass;

    public Dao(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.myEmf = MyEmf.getInstance();
        if(emf == null) {
            emf = myEmf.getEmf();
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
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