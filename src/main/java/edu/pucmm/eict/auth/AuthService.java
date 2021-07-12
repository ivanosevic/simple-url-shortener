package edu.pucmm.eict.auth;

import edu.pucmm.eict.common.MyEncryptor;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.users.UserDao;


import java.util.Optional;
import java.util.UUID;

public class AuthService {

    private static AuthService instance;
    private final UserDao userDao;
    private final MyEncryptor myEncryptor;

    private AuthService() {
        userDao = UserDao.getInstance();
        myEncryptor = MyEncryptor.getInstance();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public User login(String username, String password) {
        User user = userDao.findByUsername(username).orElseThrow(BadCredentialsException::new);
        if(!myEncryptor.compareHash(password, user.getPassword())) {
            throw new BadCredentialsException();
        }
        return user;
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