package edu.pucmm.eict.users;

import edu.pucmm.eict.common.Dao;

import java.util.Optional;

public class RoleDao extends Dao<Role, Integer> {

    private static RoleDao instance;

    private RoleDao() {
        super(Role.class);
    }

    public static RoleDao getInstance() {
        if (instance == null) {
            instance = new RoleDao();
        }
        return instance;
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
