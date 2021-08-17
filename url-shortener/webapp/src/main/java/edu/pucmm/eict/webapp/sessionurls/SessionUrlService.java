package edu.pucmm.eict.webapp.sessionurls;

import edu.pucmm.eict.urls.QrGenerator;
import edu.pucmm.eict.urls.ShortUrl;
import edu.pucmm.eict.urls.ShortUrlBuilder;
import io.javalin.http.Context;

import java.util.*;

public class SessionUrlService {

    private final ShortUrlBuilder shortUrlBuilder;
    private final QrGenerator qrGenerator;

    public SessionUrlService(ShortUrlBuilder redirectUrlBuilder, QrGenerator qrGenerator) {
        this.shortUrlBuilder = redirectUrlBuilder;
        this.qrGenerator = qrGenerator;
    }

    private String generateTemporaryCode() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public SessionUrl transform(ShortUrl shortUrl) {
        String redirectUrl = shortUrlBuilder.redirectUrl(shortUrl.getCode());
        String temporaryCode = generateTemporaryCode();
        String alias = shortUrl.getName();
        String code = shortUrl.getCode();
        String url = shortUrl.getUrl();
        String qrCode = qrGenerator.base64Qr(redirectUrl);
        return new SessionUrl(temporaryCode, alias, code, url, redirectUrl, qrCode);
    }

    public void addToSession(Context ctx, ShortUrl shortUrl) {
        List<SessionUrl> sessionUrls = getList(ctx);
        SessionUrl sessionUrl = transform(shortUrl);
        sessionUrls.add(sessionUrl);
    }

    public List<SessionUrl> getList(Context ctx) {
        List<SessionUrl> sessionUrls = ctx.sessionAttribute("sessionUrls");
        if(sessionUrls == null) {
            ctx.sessionAttribute("sessionUrls", new ArrayList<>());
            sessionUrls = ctx.sessionAttribute("sessionUrls");
        }
        return sessionUrls;
    }

    public List<SessionUrl> consult(Context ctx) {
        List<SessionUrl> sessionUrls = getList(ctx);
        List<SessionUrl> dump = new ArrayList<>(sessionUrls);
        Collections.reverse(dump);
        return dump;
    }

    public void clean(Context ctx) {
        ctx.req.getSession().removeAttribute("sessionUrls");
    }

    public Optional<SessionUrl> findByTemporaryCode(Context ctx, String temporaryCode) {
        List<SessionUrl> sessionUrls = getList(ctx);
        return sessionUrls.stream()
                .filter(s -> s.getTemporaryCode().equalsIgnoreCase(temporaryCode))
                .findFirst();
    }
}

