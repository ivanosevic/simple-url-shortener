package edu.pucmm.eict.common;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class MyEncryptor {

    private static MyEncryptor instance;
    private final ApplicationProperties appProperties;
    private final PasswordEncryptor passwordEncryptor;
    private final StandardPBEStringEncryptor encryptor;
    private final StandardPBEStringEncryptor decryptor;

    private MyEncryptor() {
        appProperties = ApplicationProperties.getInstance();
        passwordEncryptor = new StrongPasswordEncryptor();
        encryptor = new StandardPBEStringEncryptor();
        decryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(appProperties.getEncryptPass());
        decryptor.setPassword(appProperties.getEncryptPass());
    }

    public static MyEncryptor getInstance() {
        if (instance == null) {
            instance = new MyEncryptor();
        }
        return instance;
    }


    public String hash(String text) {
        return passwordEncryptor.encryptPassword(text);
    }

    public boolean compareHash(String text, String hash) {
        return passwordEncryptor.checkPassword(text, hash);
    }

    public String encrypt(String text) {
        return encryptor.encrypt(text);
    }

    public String decrypt(String text) {
        return decryptor.decrypt(text);
    }
}