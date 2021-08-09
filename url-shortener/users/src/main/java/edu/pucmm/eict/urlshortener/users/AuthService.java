package edu.pucmm.eict.urlshortener.users;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;

import java.util.Optional;
import java.util.UUID;

public class AuthService {

    private final UserDao userDao;
    private final PasswordEncryptor passwordEncryptor;
    private final AES256TextEncryptor textEncryptor;

    public AuthService(UserDao userDao, PasswordEncryptor passwordEncryptor, AES256TextEncryptor textEncryptor) {
        this.userDao = userDao;
        this.passwordEncryptor = passwordEncryptor;
        this.textEncryptor = textEncryptor;
    }

    public User login(String username, String password) {
        User user = userDao.findByUsername(username).orElseThrow(() ->
                new BadCredentialsException("The user or password are incorrect. Please, check they are correct."));
        if (!passwordEncryptor.checkPassword(password, user.getPassword())) {
            throw new BadCredentialsException("The user or password are incorrect. Please, check they are correct.");
        }
        return user;
    }

    public String assignSecret(User user) {
        String secret = UUID.randomUUID().toString();
        String encryptedSecret = textEncryptor.encrypt(secret);
        user.setSecret(secret);
        userDao.update(user);
        return encryptedSecret;
    }

    public Optional<User> findUserBySecret(String secret) {
        String decryptedSecret = textEncryptor.decrypt(secret);
        return userDao.findBySecret(decryptedSecret);
    }
}
