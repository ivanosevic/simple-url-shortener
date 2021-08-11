package edu.pucmm.eict.urlshortener.urls;

import edu.pucmm.eict.urlshortener.persistence.Page;
import edu.pucmm.eict.urlshortener.users.User;
import io.seruco.encoding.base62.Base62;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
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

    @Transactional
    public Page<ShortUrl> findPaged(int page, int size) {
        return shortUrlDao.findPaged(page, size);
    }

    @Transactional
    public Page<ShortUrl> findPagedByUserId(Long userId, int page, int size) {
        return shortUrlDao.findPagedByUserId(userId, page, size);
    }

    @Transactional
    public Optional<ShortUrl> findByCode(String code) {
        return shortUrlDao.findByCode(code);
    }

    @Transactional
    public void assignUrlsToUser(User user, List<ShortUrl> shortUrls) {
        for(var shortUrl : shortUrls) {
            shortUrl.setUser(user);
            shortUrlDao.update(shortUrl);
        }
    }

    @Transactional
    public ShortUrl delete(String code) {
        ShortUrl shortUrl = shortUrlDao.findByCode(code).orElseThrow(EntityNotFoundException::new);
        shortUrl.setActive(false);
        return shortUrlDao.update(shortUrl);
    }

    @Transactional
    public String generateCode(ShortUrl shortUrl) {
        String id = shortUrl.getId().toString();
        final byte[] encoded = base62Encoder.encode(id.getBytes());
        return new String(encoded);
    }

    @Transactional
    public ShortUrl doShort(User user, String url) {
        if(url == null || url.isBlank()) {
            throw new InvalidUrlException("The url can't be blank");
        }

        if(url.length() > 2048) {
            throw new InvalidUrlException("The url surpasses 2048 characters, and thus, is invalid.");
        }

        ShortUrl shortUrl = new ShortUrl();
        String parsedUrl = urlValidator.attachSchema(url);
        if (!urlValidator.isValid(parsedUrl)) {
            throw new InvalidUrlException("The given URL is not valid.");
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
