package edu.pucmm.eict.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.inject.Singleton;

public class CommonModule extends AbstractModule {

    @Provides
    @Singleton
    public PasswordEncryptor getPasswordEncryptor() {
        return new StrongPasswordEncryptor();
    }

    @Override
    protected void configure() {
        super.configure();
    }
}