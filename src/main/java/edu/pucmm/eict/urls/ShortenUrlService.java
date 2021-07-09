package edu.pucmm.eict.urls;

import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.common.QrCodeGenerator;
import edu.pucmm.eict.common.UrlHelper;
import edu.pucmm.eict.users.User;
import io.seruco.encoding.base62.Base62;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

public class ShortenUrlService {

    private static ShortenUrlService instance;
    private final UrlHelper urlHelper;
    private final ShortenedUrlDao shortenedUrlDao;
    private final QrCodeGenerator qrCodeGenerator;

    private ShortenUrlService() {
        urlHelper = UrlHelper.getInstance();
        shortenedUrlDao = ShortenedUrlDao.getInstance();
        qrCodeGenerator = QrCodeGenerator.getInstance();
    }

    public static ShortenUrlService getInstance() {
        if (instance == null) {
            instance = new ShortenUrlService();
        }
        return instance;
    }

    private String generateCode(Long id) {
        Base62 base62 = Base62.createInstance();
        final byte[] encoded = base62.encode(id.toString().getBytes());
        return new String(encoded);
    }

    public String getQrCodeBase64(String code) {
        ShortenedUrl shortenedUrl = shortenedUrlDao.findByCode(code).orElseThrow(EntityNotFoundException::new);
        String url = ApplicationProperties.getInstance().getRedirectDomain(shortenedUrl.getCode());
        final int width = 400;
        final int height = 400;
        return qrCodeGenerator.getQRCodeImage(url, width, height);
    }

    public Optional<ShortenedUrl> findByCode(String code) {
        return shortenedUrlDao.findByCode(code);
    }

    @Transactional
    public ShortenedUrl doShort(String url, String name, User user) {
        ShortenedUrl shortenedUrl = new ShortenedUrl();

        // Check if URL has HTTP prefix...
        String newUrl = url;
        if(!urlHelper.hasUrlPrefix(url)) {
            newUrl = "http://" + url;
        }

        // Check if given URL is valid...
        if(!urlHelper.isValid(newUrl)) {
            throw new InvalidUrlException("The given url is invalid.");
        }

        if(name == null) {
            shortenedUrl.setName(urlHelper.getHost(url));
        } else {
            shortenedUrl.setName(name);
        }

        shortenedUrl.setUser(user);
        shortenedUrl.setToUrl(newUrl);
        ShortenedUrl saved = shortenedUrlDao.create(shortenedUrl);

        // Now, we get the ID of the new entity to hash it and give it as a code
        String code = generateCode(saved.getId());
        saved.setCode(code);
        return shortenedUrlDao.update(saved);
    }
}
