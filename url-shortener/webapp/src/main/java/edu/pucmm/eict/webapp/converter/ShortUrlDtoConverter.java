package edu.pucmm.eict.webapp.converter;

import edu.pucmm.eict.urls.QrGenerator;
import edu.pucmm.eict.urls.ShortUrl;
import edu.pucmm.eict.urls.ShortUrlBuilder;
import edu.pucmm.eict.webapp.dtos.ShortUrlDto;
import org.modelmapper.AbstractConverter;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;

public class ShortUrlDtoConverter extends AbstractConverter<ShortUrl, ShortUrlDto> {

    private final QrGenerator qrGenerator;
    private final ShortUrlBuilder shortUrlBuilder;

    @Inject
    public ShortUrlDtoConverter(QrGenerator qrGenerator, ShortUrlBuilder shortUrlBuilder) {
        this.qrGenerator = qrGenerator;
        this.shortUrlBuilder = shortUrlBuilder;
    }

    @Override
    protected ShortUrlDto convert(ShortUrl shortUrl) {
        String user = "Anonymous";
        String redirectUrl = shortUrlBuilder.redirectUrl(shortUrl.getCode());
        if(shortUrl.getUser() != null) {
            user = shortUrl.getUser().getUsername();
        }
        return new ShortUrlDto(
                shortUrl.getName(),
                shortUrl.getCode(),
                qrGenerator.base64Qr(redirectUrl),
                shortUrl.getUrl(),
                redirectUrl,
                shortUrl.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                user
        );
    }
}
