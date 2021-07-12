package edu.pucmm.eict.urls.services;

import edu.pucmm.eict.common.UAAnalyzer;
import edu.pucmm.eict.common.apicalls.ApiCaller;
import edu.pucmm.eict.common.apicalls.IpApiResponse;
import edu.pucmm.eict.urls.dao.ReferrerDao;
import edu.pucmm.eict.urls.dao.ShortURLDao;
import edu.pucmm.eict.urls.models.Referrer;
import edu.pucmm.eict.urls.models.ShortURL;
import nl.basjes.parse.useragent.UserAgent;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

public class ReferrerService {

    private static ReferrerService instance;
    private final ApiCaller apiCaller;
    private final UAAnalyzer analyzer;
    private final ShortURLDao shortURLDao;
    private final ReferrerDao referrerDao;

    private ReferrerService() {
        shortURLDao = ShortURLDao.getInstance();
        referrerDao = ReferrerDao.getInstance();
        apiCaller = ApiCaller.getInstance();
        analyzer = UAAnalyzer.getInstance();
    }

    public static ReferrerService getInstance() {
        if (instance == null) {
            instance = new ReferrerService();
        }
        return instance;
    }

    @Transactional
    public ShortURL newReferrer(String code, String ip, String uaHeader) {
        ShortURL shortURL = shortURLDao.findByCode(code).orElseThrow(EntityNotFoundException::new);

        // Do API calls to get stats
        IpApiResponse geoStats = apiCaller.getGeoStats(ip);

        // Analyze User Agent Header
        UserAgent userAgent = analyzer.parse(uaHeader);
        String platform = userAgent.getValue("DeviceClass");
        String browser = userAgent.getValue("AgentName");
        String os = userAgent.getValue("OperatingSystemName");
        Referrer referrer = new Referrer(ip, browser, platform, os, geoStats.getCountry());

        referrer.setShortURL(shortURL);
        referrerDao.create(referrer);

        long increment = shortURL.getVisitCount() + 1;
        shortURL.setVisitCount(increment);
        return shortURLDao.update(shortURL);
    }
}