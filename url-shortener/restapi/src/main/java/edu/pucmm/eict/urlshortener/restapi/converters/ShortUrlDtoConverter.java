package edu.pucmm.eict.urlshortener.restapi.converters;

import edu.pucmm.eict.urlshortener.restapi.dtos.ShortUrlDto;
import edu.pucmm.eict.urlshortener.urls.RedirectUrlBuilder;
import edu.pucmm.eict.urlshortener.urls.ShortUrl;
import org.modelmapper.AbstractConverter;

import java.time.format.DateTimeFormatter;

public class ShortUrlDtoConverter extends AbstractConverter<ShortUrl, ShortUrlDto> {
    private final RedirectUrlBuilder urlBuilder;
    private final String ANONYMOUS_USER = "Anonymous";

    public ShortUrlDtoConverter(RedirectUrlBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    @Override
    protected ShortUrlDto convert(ShortUrl source) {
        String createdAt = source.getCreatedAt().format(DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss"));
        String shortUrl = urlBuilder.redirectUrl(source.getCode());
        String user = (source.getUser() != null) ? source.getUser().getUsername() : ANONYMOUS_USER;
        return new ShortUrlDto(source.getUrl(), shortUrl, createdAt, user);
    }
}
