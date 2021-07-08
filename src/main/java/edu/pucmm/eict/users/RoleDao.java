package edu.pucmm.eict.users;

import edu.pucmm.eict.common.Dao;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class RoleDao extends Dao<Role, Integer> {

    public RoleDao() {
        super(Role.class);
    }

    public Optional<Role> findByName(String name) {
        var em = this.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }
}
