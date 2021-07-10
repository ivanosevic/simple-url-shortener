package edu.pucmm.eict.urls.services;

import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.common.QrCodeGenerator;
import edu.pucmm.eict.common.URLHelper;
import edu.pucmm.eict.urls.dao.ShortURLDao;
import edu.pucmm.eict.urls.exceptions.InvalidURLException;
import edu.pucmm.eict.urls.models.ShortURL;
import edu.pucmm.eict.users.User;
import io.seruco.encoding.base62.Base62;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

public class ShortURLService {

    private static ShortURLService instance;
    private final URLHelper urlHelper;
    private final ShortURLDao shortURLDao;
    private final QrCodeGenerator qrCodeGenerator;

    private ShortURLService() {
        urlHelper = URLHelper.getInstance();
        shortURLDao = ShortURLDao.getInstance();
        qrCodeGenerator = QrCodeGenerator.getInstance();
    }

    public static ShortURLService getInstance() {
        if (instance == null) {
            instance = new ShortURLService();
        }
        return instance;
    }

    private String generateCode(Long id) {
        Base62 base62 = Base62.createInstance();
        final byte[] encoded = base62.encode(id.toString().getBytes());
        return new String(encoded);
    }

    public String getQrCodeBase64(String code) {
        ShortURL shortURL = shortURLDao.findByCode(code).orElseThrow(EntityNotFoundException::new);
        String url = ApplicationProperties.getInstance().getRedirectDomain(shortURL.getCode());
        return qrCodeGenerator.getQRCodeImage(url, 400, 400);
    }

    public Optional<ShortURL> findByCode(String code) {
        return shortURLDao.findByCode(code);
    }

    @Transactional
    public ShortURL cut(String url, String name, User user) {
        ShortURL shortURL = new ShortURL();

        // Check if URL has HTTP prefix.
        // If not, we attach the http prefix, because port 80 may also
        // redirect to https
        String newUrl = url;
        if(!urlHelper.hasUrlPrefix(url)) {
            newUrl = "http://" + url;
        }

        // Check if given URL is valid...
        if(!urlHelper.isValid(newUrl)) {
            throw new InvalidURLException("The given url is invalid.");
        }

        if(name == null) {
            shortURL.setName(urlHelper.getHost(url));
        } else {
            shortURL.setName(name);
        }

        shortURL.setUser(user);
        shortURL.setToURL(newUrl);
        ShortURL saved = shortURLDao.create(shortURL);

        // Now, we get the ID of the new entity to hash it and give it as a code
        String code = generateCode(saved.getId());
        saved.setCode(code);
        return shortURLDao.update(saved);
    }
}