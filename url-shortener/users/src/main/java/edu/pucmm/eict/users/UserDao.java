package edu.pucmm.eict.users;


import edu.pucmm.eict.persistence.Page;
import edu.pucmm.eict.persistence.PaginationDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class UserDao extends PaginationDao<User, Long> {

    public UserDao(EntityManagerFactory entityManagerFactory) {
        super(User.class, entityManagerFactory);
    }

    public Page<User> findPaged(int page, int size) {
        EntityManager em = super.getEntityManager();
        try {
            TypedQuery<Long> count = em.createQuery("select count(u) from User u where u.deletedAt = 0L", Long.class);
            TypedQuery<Long> ids = em.createQuery("select u.id from User u where u.deletedAt = 0L group by u.id order by max(u.joinedAt) desc", Long.class);
            TypedQuery<User> query = em.createQuery("select u from User u join fetch u.roles as roles where u.id in (:ids) group by u.id order by max(u.joinedAt) desc", User.class);
            return super.findPagedFilter(page, size, count, ids, query);
        } finally {
            em.close();
        }
    }

    public Optional<User> findById(Long id) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("select u from User u join fetch u.roles as roles where u.deletedAt = 0L and u.id = :id", User.class)
                    .setParameter("id", id)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<User> findByUsername(String username) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("select u from User u join fetch u.roles as roles where u.deletedAt = 0L and u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<User> findByEmail(String email) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("select u from User u join fetch u.roles as roles where u.deletedAt = 0L and u.email = :email", User.class)
                    .setParameter("email", email)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<User> findBySecret(String secret) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("select u from User u join fetch u.roles as roles where u.deletedAt = 0L and u.secret = :secret", User.class)
                    .setParameter("secret", secret)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }
}

