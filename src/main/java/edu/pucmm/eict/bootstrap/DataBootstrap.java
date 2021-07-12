package edu.pucmm.eict.bootstrap;


import edu.pucmm.eict.users.Role;
import edu.pucmm.eict.users.RoleDao;
import edu.pucmm.eict.users.UserForm;
import edu.pucmm.eict.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class DataBootstrap {

    private static final Logger log = LoggerFactory.getLogger(DataBootstrap.class);
    private static DataBootstrap instance;

    private final RoleDao roleDao;
    private final UserService userService;

    private DataBootstrap() {
        roleDao = RoleDao.getInstance();
        userService = UserService.getInstance();
    }

    public static DataBootstrap getInstance() {
        if (instance == null) {
            instance = new DataBootstrap();
        }
        return instance;
    }

    public void inserts() {
        this.insertRoles();
        this.insertUsers();
    }

    private void insertRoles() {
        log.info("Inserting application roles.");
        List<Role> roles = List.of(Role.APP_USER, Role.ADMIN);
        roles.forEach(role -> {
            if(roleDao.findByName(role.getName()).isEmpty()) {
                roleDao.create(role);
                log.info("Role: {} created.", role);
            }
        });
    }

    private void insertUsers() {
        log.info("Inserting default users.");
        UserForm userForm = new UserForm("Admin", "J2LgNFg2Zv8AkEYf",
                "20170874@ce.pucmm.edu.do", "Ivanosevic", "Garcia", Set.of("ADMIN"));
        if(userService.findByUsername(userForm.getUsername()).isEmpty()) {
            userService.create(userForm);
            log.info("User: {} created.", userForm);
        }
    }
}