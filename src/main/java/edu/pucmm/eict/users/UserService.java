package edu.pucmm.eict.users;

import edu.pucmm.eict.common.MyEncryptor;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UserService {

    private static UserService instance;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final MyEncryptor myEncryptor;

    private UserService() {
        userDao = UserDao.getInstance();
        roleDao = RoleDao.getInstance();
        myEncryptor = MyEncryptor.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    @Transactional
    public User create(UserForm form) {
        if(userDao.findByUsername(form.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with username: " + form.getUsername() + " already exists");
        }

        if(userDao.findByEmail(form.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email: " + form.getEmail() + " already exists");
        }

        String hashedPassword = myEncryptor.hash(form.getPassword());
        User user = new User(form.getUsername(), form.getEmail(), hashedPassword, form.getName(), form.getLastname());
        form.getRoles().forEach(s -> {
            var role = roleDao.findByName(s).orElseThrow(() -> new EntityNotFoundException("Role: " +  s + " couldn't be found."));
            user.getRoles().add(role);
        });
        return userDao.create(user);
    }

    @Transactional
    public User update(Long id, UserForm form) {
        User user = userDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Username: " + id + " couldn't be found."));
        var dbUsername = userDao.findByUsername(form.getUsername());
        var dbEmail = userDao.findByEmail(form.getEmail());

        if(dbUsername.isPresent() && !dbUsername.get().getUsername().equalsIgnoreCase(user.getUsername())) {
            throw new UserAlreadyExistsException("User with username: " + form.getUsername() + " already exists");
        }

        if(dbEmail.isPresent() && !dbEmail.get().getEmail().equalsIgnoreCase(user.getEmail())) {
            throw new UserAlreadyExistsException("User with email: " + form.getEmail() + " already exists");
        }

        Set<Role> roles = new HashSet<>();
        form.getRoles().forEach(s -> {
            var role = roleDao.findByName(s).orElseThrow(() -> new EntityNotFoundException("Role: " +  s + " couldn't be found."));
            roles.add(role);
        });

        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setName(form.getName());
        user.setPassword(form.getLastname());
        user.setRoles(roles);
        return userDao.update(user);
    }

    @Transactional
    public User delete(String username) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Username: " + username + " couldn't be found."));
        Long deletedAt = System.currentTimeMillis();
        user.setDeletedAt(deletedAt);
        return userDao.update(user);
    }
}