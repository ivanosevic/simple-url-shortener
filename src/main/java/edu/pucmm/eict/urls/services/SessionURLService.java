package edu.pucmm.eict.urls.services;

import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.urls.models.SessionURL;
import edu.pucmm.eict.urls.models.ShortURL;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SessionURLService {

    private static SessionURLService instance;
    private final ApplicationProperties appProperties;

    private SessionURLService() {
        this.appProperties = ApplicationProperties.getInstance();
    }

    public static SessionURLService getInstance() {
        if (instance == null) {
            instance = new SessionURLService();
        }
        return instance;
    }

    public SessionURL transform(ShortURL shortURL) {
        String newURL = appProperties.getRedirectDomain(shortURL.getCode());
        SessionURL sessionURL = new SessionURL();
        sessionURL.setId(shortURL.getId());
        sessionURL.setCode(shortURL.getCode());
        sessionURL.setToUrl(shortURL.getToURL());
        sessionURL.setName(shortURL.getName());
        sessionURL.setNewUrl(newURL);
        return sessionURL;
    }

    public void clean(Context ctx) {
        ctx.req.getSession().removeAttribute("urls");
    }

    public List<SessionURL> findSessionURLs(Context ctx) {
        List<SessionURL> urls = ctx.sessionAttribute("urls");
        if(urls != null) {
            Collections.reverse(urls);
        }
        return urls;
    }

    public Optional<SessionURL> findByTemporaryId(Context ctx, String id) {
        var sessionURLs = this.findSessionURLs(ctx);
        if(sessionURLs == null) {
            return Optional.empty();
        }
        return sessionURLs.stream()
                .filter(s -> s.getTemporaryCode().equalsIgnoreCase(id))
                .findFirst();
    }

    public void addSessionURL(Context ctx, ShortURL shortURL) {
        // Transform the request short url
        SessionURL sessionURL = this.transform(shortURL);

        // Fetch from context
        List<SessionURL> sessionURLs = ctx.sessionAttribute("urls");
        if(sessionURLs == null) {
            sessionURLs = new ArrayList<>();
            ctx.sessionAttribute("urls", sessionURLs);
        }
        // finally, add to the list.
        sessionURLs.add(sessionURL);
    }
}