package edu.pucmm.eict.users;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

public class AuthService {

    private final UserDao userDao;
    private final MyEncryptor myEncryptor;

    @Inject
    public AuthService(UserDao userDao, MyEncryptor myEncryptor) {
        this.userDao = userDao;
        this.myEncryptor = myEncryptor;
    }

    public User login(String username, String password) {
        User user = userDao.findByUsername(username).orElseThrow(() ->
                new BadCredentialsException("The user or password are incorrect. Please, check they are correct."));
        if (!myEncryptor.compareHash(password, user.getPassword())) {
            throw new BadCredentialsException("The user or password are incorrect. Please, check they are correct.");
        }
        return user;
    }

    public void invalidateSecret(User user) {
        user.setSecret(null);
        userDao.update(user);
    }

    public String assignSecret(User user) {
        String secret = UUID.randomUUID().toString();
        String encryptedSecret = myEncryptor.encrypt(secret);
        user.setSecret(secret);
        userDao.update(user);
        return encryptedSecret;
    }

    public Optional<User> findUserBySecret(String secret) {
        String decryptedSecret = myEncryptor.decrypt(secret);
        return userDao.findBySecret(decryptedSecret);
    }
}