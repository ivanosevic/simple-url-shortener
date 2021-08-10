package edu.pucmm.eict.urlshortener.users;

import edu.pucmm.eict.urlshortener.persistence.Page;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;
    private final MyEncryptor myEncryptor;

    public UserService(UserDao userDao, MyEncryptor myEncryptor) {
        this.userDao = userDao;
        this.myEncryptor = myEncryptor;
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public Page<User> findPaged(int page, int size) {
        return userDao.findPaged(page, size);
    }

    private void hashUserPassword(User user) {
        String password = user.getPassword();
        String hashedPassword = myEncryptor.hash(password);
        user.setPassword(hashedPassword);
    }

    private void addRolesToUser(User user, List<Role> roles) {
        user.setRoles(new HashSet<>(roles));
    }

    public boolean userExists(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        return userDao.findByEmail(email).isPresent() || userDao.findByUsername(username).isPresent();
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Transactional
    public User create(User user) {
        if(userExists(user)) {
            throw new UserAlreadyExistsException("User with username = " + user.getUsername() + " or email = " + user.getEmail());
        }
        hashUserPassword(user);
        addRolesToUser(user, List.of(RoleList.APP_USER));
        return userDao.create(user);
    }

    @Transactional
    public User createAsAdmin(User user) {
        if(userExists(user)) {
            throw new UserAlreadyExistsException("User with username = " + user.getUsername() + " or email = " + user.getEmail());
        }
        hashUserPassword(user);
        addRolesToUser(user, List.of(RoleList.ADMIN));
        return userDao.create(user);
    }

    @Transactional
    public User delete(User user) {
        Long deletedAt = System.currentTimeMillis() / 1000L;
        user.setDeletedAt(deletedAt);
        return userDao.update(user);
    }
}
