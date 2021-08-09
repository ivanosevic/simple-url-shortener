package edu.pucmm.eict.urlshortener.urls;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

public class RedirectService {
    private final ClickDao clickDao;
    private final ShortUrlDao shortUrlDao;
    private final IpInfoService ipInfoService;
    private final UserAgentAnalyzer uaa;

    public RedirectService(ClickDao clickDao,
                           ShortUrlDao shortUrlDao,
                           IpInfoService ipInfoService,
                           UserAgentAnalyzer uaa) {
        this.clickDao = clickDao;
        this.shortUrlDao = shortUrlDao;
        this.ipInfoService = ipInfoService;
        this.uaa = uaa;
    }

    @Transactional
    public ShortUrl getClickInfo(String code, String ip, String uaHeader) {
        ShortUrl shortUrl = shortUrlDao.findByCode(code).orElseThrow(EntityNotFoundException::new);
        IpInfo ipInfo = ipInfoService.getInfoFromIp(ip);
        UserAgent userAgent = uaa.parse(uaHeader);
        String platform = userAgent.getValue("DeviceClass");
        String browser = userAgent.getValue("AgentName");
        String os = userAgent.getValue("OperatingSystemName");
        String country = ipInfo.getCountry();
        String countryIso2 = ipInfo.getCountryCode();
        Click click = new Click(shortUrl, ip, browser, platform, os, country, countryIso2);
        clickDao.create(click);
        return shortUrl;
    }
}
