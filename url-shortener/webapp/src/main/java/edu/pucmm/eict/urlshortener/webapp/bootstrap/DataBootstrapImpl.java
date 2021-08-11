package edu.pucmm.eict.urlshortener.webapp.bootstrap;

import edu.pucmm.eict.urlshortener.users.*;

import java.util.List;
import java.util.Set;

public class DataBootstrapImpl implements DataBootstrap {

    private final RoleDao roleDao;
    private final UserService userService;

    public DataBootstrapImpl(RoleDao roleDao, UserService userService) {
        this.roleDao = roleDao;
        this.userService = userService;
    }

    private void insertRoles() {
        List<Role> roles = List.of(RoleList.APP_USER, RoleList.ADMIN);
        roles.forEach(role -> {
            String roleName = role.getName();
            var roleDb = roleDao.findByName(roleName);
            if(roleDb.isEmpty()) {
                roleDao.create(role);
            }
        });
    }

    private void insertUsers() {
        User user = UserList.ADMIN;
        user.setRoles(Set.of(RoleList.ADMIN));
        var userDb = userService.findByUsername(user.getUsername());
        if(userDb.isEmpty()) {
            userService.createAsAdmin(user);
        }
    }

    @Override
    public void inserts() {
        insertRoles();
        insertUsers();
    }
}
