package edu.pucmm.eict.urlshortener.users;

import edu.pucmm.eict.urlshortener.persistence.Dao;

import javax.persistence.EntityManagerFactory;
import java.util.Optional;

public class RoleDao extends Dao<Role, Integer> {

    public RoleDao(EntityManagerFactory entityManagerFactory) {
        super(Role.class, entityManagerFactory);
    }

    public Optional<Role> findByName(String name) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("select r from Role r where r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }
}
