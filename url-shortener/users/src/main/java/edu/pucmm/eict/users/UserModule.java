package edu.pucmm.eict.users;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import edu.pucmm.eict.persistence.Dao;
import edu.pucmm.eict.persistence.PaginationDao;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.inject.Named;
import javax.inject.Singleton;

public class UserModule extends AbstractModule {

    @Provides
    @Singleton
    public PasswordEncryptor getStrongPasswordEncryptor() {
        return new StrongPasswordEncryptor();
    }

    @Provides
    @Singleton
    public StandardPBEStringEncryptor getStandardPBEStringEncryptor(@Named("passwordEncryptor") String passwordEncryptor) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setPassword(passwordEncryptor);
        return standardPBEStringEncryptor;
    }

    @Override
    protected void configure() {
        bind(new TypeLiteral<PaginationDao<User, Long>>(){}).to(new TypeLiteral<UserDao>(){}).in(Singleton.class);
        bind(new TypeLiteral<Dao<Role, Integer>>(){}).to(new TypeLiteral<RoleDao>(){}).in(Singleton.class);
        bind(MyEncryptor.class).to(JasyptEncryptor.class).in(Singleton.class);
        bind(AuthService.class).in(Singleton.class);
        bind(UserService.class).in(Singleton.class);
    }
}
