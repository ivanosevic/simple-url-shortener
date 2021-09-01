package edu.pucmm.eict.soap.dto;

import edu.pucmm.eict.urls.ShortUrl;
import edu.pucmm.eict.urls.ShortUrlBuilder;
import org.modelmapper.AbstractConverter;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;

public class ShortUrlDtoConverter extends AbstractConverter<ShortUrl, ShortUrlDto> {

    private final ShortUrlBuilder shortUrlBuilder;
    private final String ANONYMOUS_USER = "Anonymous";

    @Inject
    public ShortUrlDtoConverter(ShortUrlBuilder shortUrlBuilder) {
        this.shortUrlBuilder = shortUrlBuilder;
    }

    @Override
    protected ShortUrlDto convert(ShortUrl source) {
        String createdAt = source.getCreatedAt().format(DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss"));
        String shortUrl = shortUrlBuilder.redirectUrl(source.getCode());
        String user = (source.getUser() != null) ? source.getUser().getUsername() : ANONYMOUS_USER;
        String preview = source.getPreview();
        return new ShortUrlDto(source.getUrl(), shortUrl, createdAt, user, preview);
    }
}
