package edu.pucmm.eict.urlshortener.webapp.converters;

import edu.pucmm.eict.urlshortener.urls.QrGenerator;
import edu.pucmm.eict.urlshortener.urls.RedirectUrlBuilder;
import edu.pucmm.eict.urlshortener.urls.ShortUrl;
import edu.pucmm.eict.urlshortener.webapp.domain.ShortUrlDto;
import org.modelmapper.AbstractConverter;

import java.time.format.DateTimeFormatter;

public class ShortUrlDtoConverter extends AbstractConverter<ShortUrl, ShortUrlDto> {

    private final QrGenerator qrGenerator;
    private final RedirectUrlBuilder redirectUrlBuilder;

    public ShortUrlDtoConverter(QrGenerator qrGenerator, RedirectUrlBuilder redirectUrlBuilder) {
        this.qrGenerator = qrGenerator;
        this.redirectUrlBuilder = redirectUrlBuilder;
    }

    @Override
    protected ShortUrlDto convert(ShortUrl shortUrl) {
        String user = "Anonymous";
        String redirectUrl = redirectUrlBuilder.redirectUrl(shortUrl.getCode());
        if(shortUrl.getUser() != null) {
            user = shortUrl.getUser().getUsername();
        }
        return new ShortUrlDto(
                shortUrl.getName(),
                shortUrl.getCode(),
                qrGenerator.getQrCode(redirectUrl),
                shortUrl.getUrl(),
                redirectUrl,
                shortUrl.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                user
        );
    }
}
