package edu.pucmm.eict.urls;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import edu.pucmm.eict.persistence.Dao;
import edu.pucmm.eict.persistence.PaginationDao;
import io.seruco.encoding.base62.Base62;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class UrlModule extends AbstractModule {

    @Provides
    @Singleton
    public Client getAPIClient() {
        return ClientBuilder.newClient();
    }

    @Provides
    @Singleton
    public Base62 getBase62Encoder() {
        return Base62.createInstance();
    }

    @Provides
    @Singleton
    public UserAgentAnalyzer getUserAgentAnalyzer() {
        return UserAgentAnalyzer.newBuilder().withCache(1234)
                .withField("DeviceClass")
                .withAllFields()
                .build();
    }

    @Override
    protected void configure() {
        bind(new TypeLiteral<Dao<Click, Long>>(){}).to(new TypeLiteral<ClickDao>(){}).in(Singleton.class);
        bind(new TypeLiteral<PaginationDao<ShortUrl, Long>>(){}).to(new TypeLiteral<ShortUrlDao>(){}).in(Singleton.class);
        bind(QrGenerator.class).to(PngQrGenerator.class).in(Singleton.class);
        bind(IpInfoService.class).to(IpInfoServiceImpl.class).in(Singleton.class);
        bind(ShortUrlBuilder.class).to(ShortUrlBuilderImpl.class).in(Singleton.class);
        bind(UrlEncoder.class).to(Base62UrlEncoder.class).in(Singleton.class);
        bind(UrlPreviewer.class).to(UrlPreviewerImpl.class).in(Singleton.class);
        bind(MyUrlValidator.class).in(Singleton.class);
        bind(ShortUrlService.class).in(Singleton.class);
        bind(RedirectService.class).in(Singleton.class);
    }
}
