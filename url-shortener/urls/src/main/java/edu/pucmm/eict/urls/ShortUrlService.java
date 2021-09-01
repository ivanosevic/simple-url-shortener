package edu.pucmm.eict.urls;

import edu.pucmm.eict.persistence.Page;
import edu.pucmm.eict.users.User;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class ShortUrlService {

    private final ShortUrlDao shortUrlDao;
    private final UrlEncoder urlEncoder;
    private final MyUrlValidator myUrlValidator;
    private final UrlPreviewer urlPreviewer;

    @Inject
    public ShortUrlService(ShortUrlDao shortUrlDao, UrlEncoder urlEncoder, MyUrlValidator myUrlValidator, UrlPreviewer urlPreviewer) {
        this.shortUrlDao = shortUrlDao;
        this.urlEncoder = urlEncoder;
        this.myUrlValidator = myUrlValidator;
        this.urlPreviewer = urlPreviewer;
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
    public ShortUrl doShort(User user, String url) {
        if(url == null || url.isBlank()) {
            throw new InvalidUrlException("The url can't be blank");
        }

        if(url.length() > 2048) {
            throw new InvalidUrlException("The url surpasses 2048 characters, and thus, is invalid.");
        }

        ShortUrl shortUrl = new ShortUrl();
        String parsedUrl = myUrlValidator.attachSchema(url);
        if (!myUrlValidator.isValid(parsedUrl)) {
            throw new InvalidUrlException("The given URL is not valid.");
        }

        String alias = myUrlValidator.getHost(parsedUrl);
        shortUrl.setName(alias);
        shortUrl.setUser(user);
        shortUrl.setUrl(parsedUrl);
        shortUrl.setPreview(urlPreviewer.getPreviewImg(url));
        ShortUrl saved = shortUrlDao.create(shortUrl);

        // Now, we generate and assign the code
        String id = shortUrl.getId().toString();
        String code = urlEncoder.getCode(id);
        saved.setCode(code);
        return shortUrlDao.update(saved);
    }
}
