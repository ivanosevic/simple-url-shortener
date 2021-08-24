package edu.pucmm.eict.users;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.password.PasswordEncryptor;

import javax.inject.Inject;

public class JasyptEncryptor implements MyEncryptor {

    private final PasswordEncryptor passwordEncryptor;
    private final StandardPBEStringEncryptor textEncryptor;
    private final String EMPTY = "";

    @Inject
    public JasyptEncryptor(PasswordEncryptor passwordEncryptor, StandardPBEStringEncryptor textEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
        this.textEncryptor = textEncryptor;
    }

    @Override
    public boolean compareHash(String text, String data) {
        return passwordEncryptor.checkPassword(text, data);
    }

    @Override
    public String encrypt(String text) {
        return textEncryptor.encrypt(text);
    }

    @Override
    public String decrypt(String text) {
        try {
            return textEncryptor.decrypt(text);
        } catch (EncryptionOperationNotPossibleException ex) {
            return EMPTY;
        }
    }

    @Override
    public String hash(String text) {
        return passwordEncryptor.encryptPassword(text);
    }
}
