package edu.pucmm.eict.common;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
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

    public Page<T> findPaged(Integer page, Integer size, TypedQuery<Long> count, TypedQuery<T> query) {
        if(page <= 0) {
            throw new PaginationErrorException("The page number can't be less or equal to 0.");
        }

        List<Long> countResult = count.getResultList();
        long amount = 0;
        if(countResult.size() == 1) {
            amount = countResult.get(0);
        } else if(countResult.size() > 1) {
            amount = countResult.size();
        } else {
            // Page is empty...
            return new Page<>(true);
        }

        long pages = (size + amount - 1) / size;
        if(page > pages) {
            throw new PaginationErrorException("THe number of pages can't exceed total");
        }
        int offset = (page - 1) * size;
        boolean isFirst = page == 1;
        boolean isLast = page == pages;

        List<T> results = query.setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        return new Page<>((int) pages, page, isFirst, isLast, results);
    }

    public Page<T> findPagedFilter(Integer page, Integer size, TypedQuery<Long> count, TypedQuery<PK> ids, TypedQuery<T> query) {
        if(page <= 0) {
            throw new PaginationErrorException("The page number can't be less or equal to 0.");
        }

        List<Long> countResult = count.getResultList();
        long amount = 0;
        if(countResult.size() == 1) {
            amount = countResult.get(0);
        } else if(countResult.size() > 1) {
            amount = countResult.size();
        } else {
            // Page is empty...
            return new Page<>(true);
        }

        long pages = (size + amount - 1) / size;
        if(pages == 0) {
            return new Page<>(true);
        }
        if(page > pages) {
            throw new PaginationErrorException("THe number of pages can't exceed total");
        }
        int offset = (page - 1) * size;
        boolean isFirst = page == 1;
        boolean isLast = page == pages;

        List<PK> idsResults = ids.setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();

        List<T> results = query.setParameter("ids", idsResults).getResultList();
        return new Page<>((int) pages, page, isFirst, isLast, results);
    }
}