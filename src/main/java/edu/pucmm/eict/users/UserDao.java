package edu.pucmm.eict.users;

import edu.pucmm.eict.common.Dao;
import edu.pucmm.eict.common.Page;
import org.hibernate.jpa.QueryHints;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UserDao extends Dao<User, Long> {

    private static UserDao instance;

    private UserDao() {
        super(User.class);
    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    public Page<User> findPaged(Integer page, Integer size, Long selfUserId) {
        var em = this.getEntityManager();
        try {
            TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.deletedAt = :active AND u.id != :selfUserId", Long.class)
                    .setParameter("active", 0L)
                    .setParameter("selfUserId", selfUserId);

            TypedQuery<Long> ids = em.createQuery("SELECT u.id FROM User u WHERE u.deletedAt = :active AND u.id != :selfUserId GROUP BY u.id ORDER BY MAX(u.joinedAt) DESC", Long.class)
                    .setParameter("active", 0L)
                    .setParameter("selfUserId", selfUserId);

            TypedQuery<User> typedQuery = em.createQuery("SELECT DISTINCT u FROM User u JOIN FETCH u.roles as roles WHERE u.deletedAt = :active AND u.id IN (:ids) GROUP BY u.id ORDER BY MAX(u.joinedAt) DESC", User.class)
                    .setParameter("active", 0L)
                    .setHint(
                            QueryHints.HINT_PASS_DISTINCT_THROUGH,
                            false
                    );
            return super.findPagedFilter(page, size, countQuery, ids, typedQuery);
        } finally {
            em.close();
        }
    }

    public List<User> findAll() {
        var em = this.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u JOIN FETCH u.roles as roles WHERE u.deletedAt = :active", User.class)
                    .setParameter("active", 0L)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<User> findById(Long id) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u JOIN FETCH u.roles as roles WHERE u.deletedAt = :active AND u.id = :id", User.class)
                    .setParameter("active", 0L)
                    .setParameter("id", id)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<User> findByUsername(String username) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u JOIN FETCH u.roles as roles WHERE u.deletedAt = :active AND u.username = :username", User.class)
                    .setParameter("active", 0L)
                    .setParameter("username", username)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<User> findByEmail(String email) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u JOIN FETCH u.roles as roles WHERE u.deletedAt = :active AND u.email = :email", User.class)
                    .setParameter("active", 0L)
                    .setParameter("email", email)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<User> findBySecret(String secret) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u JOIN FETCH u.roles as roles WHERE u.deletedAt = :active AND u.secret = :secret", User.class)
                    .setParameter("active", 0L)
                    .setParameter("secret", secret)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }
}