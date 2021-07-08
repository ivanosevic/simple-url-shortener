package edu.pucmm.eict.bootstrap;


import edu.pucmm.eict.users.Role;
import edu.pucmm.eict.users.RoleDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class DataBootstrap {

    private final RoleDao roleDao;

    @Inject
    public DataBootstrap(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void inserts() {
        this.insertRoles();
    }

    private void insertRoles() {
        List<Role> roles = List.of(Role.APP_USER, Role.ADMIN);
        roles.forEach(role -> {
            if(roleDao.findByName(role.getName()).isEmpty()) {
                roleDao.create(role);
            }
        });
    }
}
