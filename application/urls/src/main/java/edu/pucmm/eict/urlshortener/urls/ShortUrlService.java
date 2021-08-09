package edu.pucmm.eict.urlshortener.urls;

import edu.pucmm.eict.urlshortener.persistence.Page;
import edu.pucmm.eict.urlshortener.users.User;
import io.seruco.encoding.base62.Base62;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

public class ShortUrlService {

    private final ShortUrlDao shortUrlDao;
    private final Base62 base62Encoder;
    private final UrlValidator urlValidator;

    public ShortUrlService(ShortUrlDao shortUrlDao, Base62 base62Encoder, UrlValidator urlValidator) {
        this.shortUrlDao = shortUrlDao;
        this.base62Encoder = base62Encoder;
        this.urlValidator = urlValidator;
    }

    public Page<ShortUrl> findPaged(int page, int size) {
        return shortUrlDao.findPaged(page, size);
    }

    public Optional<ShortUrl> findByCode(String code) {
        return shortUrlDao.findByCode(code);
    }

    public ShortUrl delete(String code) {
        ShortUrl shortUrl = shortUrlDao.findByCode(code).orElseThrow(EntityNotFoundException::new);
        shortUrl.setActive(false);
        return shortUrlDao.update(shortUrl);
    }

    public String generateCode(ShortUrl shortUrl) {
        String id = shortUrl.getId().toString();
        final byte[] encoded = base62Encoder.encode(id.getBytes());
        return new String(encoded);
    }

    public ShortUrl doShort(User user, String url) {
        ShortUrl shortUrl = new ShortUrl();
        String parsedUrl = urlValidator.attachSchema(url);
        if (!urlValidator.isValid(parsedUrl)) {
            throw new InvalidUrlException("The given URL: " + url + " is not valid.");
        }

        String alias = urlValidator.getHost(parsedUrl);
        shortUrl.setName(alias);
        shortUrl.setUser(user);
        shortUrl.setUrl(parsedUrl);
        ShortUrl saved = shortUrlDao.create(shortUrl);

        // Now, we generate and assign the code
        String code = generateCode(shortUrl);
        saved.setCode(code);
        return shortUrlDao.update(saved);
    }
}
