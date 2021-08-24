package edu.pucmm.eict.grpc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.pucmm.eict.persistence.PersistenceModule;
import edu.pucmm.eict.reports.ReportModule;
import edu.pucmm.eict.urls.UrlModule;
import edu.pucmm.eict.users.UserModule;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new PersistenceModule(),
                new UserModule(),
                new UrlModule(),
                new ReportModule(),
                new AppModule()
        );

        injector.getInstance(AppStartup.class).start(args);
    }
}
