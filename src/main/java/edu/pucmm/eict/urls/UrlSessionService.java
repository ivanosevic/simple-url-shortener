package edu.pucmm.eict.urls;

import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class UrlSessionService {

    private static UrlSessionService instance;

    private UrlSessionService() {
    }

    public static UrlSessionService getInstance() {
        if (instance == null) {
            instance = new UrlSessionService();
        }
        return instance;
    }

    public List<ShortenedUrl> shortenedUrls(Context ctx) {
        return ctx.sessionAttribute("urls");
    }

    private void addUrl(Context ctx, ShortenedUrl url) {
        List<ShortenedUrl> urls = ctx.sessionAttribute("urls");
        if(urls == null) {
            urls = new ArrayList<>();
            ctx.sessionAttribute("urls", urls);
        }
        urls.add(url);
    }
}
