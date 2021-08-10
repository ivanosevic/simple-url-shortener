package edu.pucmm.eict.urlshortener.webapp.converters;

import edu.pucmm.eict.urlshortener.urls.QrGenerator;
import edu.pucmm.eict.urlshortener.urls.RedirectUrlBuilder;
import edu.pucmm.eict.urlshortener.urls.ShortUrl;
import edu.pucmm.eict.urlshortener.webapp.ShortUrlDto;
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
        return new ShortUrlDto(
                        shortUrl.getName(),
                        shortUrl.getCode(),
                        qrGenerator.getQrCode(shortUrl.getCode()),
                        shortUrl.getUrl(),
                        redirectUrlBuilder.redirectUrl(shortUrl.getCode()),
                        shortUrl.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
        );
    }
}
