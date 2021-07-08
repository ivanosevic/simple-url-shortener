package edu.pucmm.eict;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.pucmm.eict.common.CommonModule;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppModule(), new DbModule(), new CommonModule());
        injector.getInstance(DbStartup.class).boot(args);
        injector.getInstance(Startup.class).boot(args);
    }
}