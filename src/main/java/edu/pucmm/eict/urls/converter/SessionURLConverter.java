package edu.pucmm.eict.urls.converter;

import edu.pucmm.eict.common.ApplicationProperties;
import edu.pucmm.eict.urls.models.SessionURL;
import edu.pucmm.eict.urls.models.ShortURL;

public class SessionURLConverter {

    private static SessionURLConverter instance;
    private final ApplicationProperties appProperties;

    private SessionURLConverter() {
        this.appProperties = ApplicationProperties.getInstance();
    }

    public SessionURL convert(ShortURL shortURL) {
        SessionURL dto = new SessionURL();
        dto.setId(shortURL.getId());
        dto.setCode(shortURL.getCode());
        dto.setToUrl(shortURL.getToUrl());
        dto.setName(shortURL.getName());
        dto.setNewUrl(appProperties.getRedirectDomain(shortURL.getCode()));
        return dto;
    }

    public static SessionURLConverter getInstance() {
        if (instance == null) {
            instance = new SessionURLConverter();
        }
        return instance;
    }
}