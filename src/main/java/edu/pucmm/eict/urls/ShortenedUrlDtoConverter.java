package edu.pucmm.eict.urls;

import edu.pucmm.eict.common.ApplicationProperties;

public class ShortenedUrlDtoConverter {

    private static ShortenedUrlDtoConverter shortenedUrlDtoConverterInstance;
    private final ApplicationProperties appProperties;

    private ShortenedUrlDtoConverter() {
        this.appProperties = ApplicationProperties.getInstance();
    }

    public ShortenedUrlDto convert(ShortenedUrl shortenedUrl) {
        ShortenedUrlDto dto = new ShortenedUrlDto();
        dto.setId(shortenedUrl.getId());
        dto.setCode(shortenedUrl.getCode());
        dto.setToUrl(shortenedUrl.getToUrl());
        dto.setName(shortenedUrl.getName());
        if(shortenedUrl.getUser() == null) {
            dto.setUsername(null);
        } else {
            dto.setUsername(shortenedUrl.getUser().getUsername());
        }
        dto.setNewUrl(appProperties.getRedirectDomain(shortenedUrl.getCode()));
        return dto;
    }

    public static ShortenedUrlDtoConverter getInstance() {
        if (shortenedUrlDtoConverterInstance == null) {
            shortenedUrlDtoConverterInstance = new ShortenedUrlDtoConverter();
        }
        return shortenedUrlDtoConverterInstance;
    }
}