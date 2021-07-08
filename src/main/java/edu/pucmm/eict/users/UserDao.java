package edu.pucmm.eict.users;

import edu.pucmm.eict.common.Dao;

import java.util.List;
import java.util.Optional;

public class UserDao extends Dao<User, Long> {

    public UserDao() {
        super(User.class);
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