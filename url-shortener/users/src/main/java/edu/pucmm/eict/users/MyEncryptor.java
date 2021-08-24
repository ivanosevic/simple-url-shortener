package edu.pucmm.eict.users;

public interface MyEncryptor {
    boolean compareHash(String text, String data);
    String encrypt(String text);
    String decrypt(String text);
    String hash(String text);
}
