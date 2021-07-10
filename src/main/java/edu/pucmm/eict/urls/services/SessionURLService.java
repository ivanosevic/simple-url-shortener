package edu.pucmm.eict.urls.services;

import edu.pucmm.eict.urls.models.ShortURL;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class SessionURLService {

    private static SessionURLService instance;

    private SessionURLService() {
    }

    public static SessionURLService getInstance() {
        if (instance == null) {
            instance = new SessionURLService();
        }
        return instance;
    }

    public List<ShortURL> shortenedUrls(Context ctx) {
        return ctx.sessionAttribute("urls");
    }

    private void addUrl(Context ctx, ShortURL url) {
        List<ShortURL> urls = ctx.sessionAttribute("urls");
        if(urls == null) {
            urls = new ArrayList<>();
            ctx.sessionAttribute("urls", urls);
        }
        urls.add(url);
    }
}