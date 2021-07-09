package edu.pucmm.eict.urls;

import edu.pucmm.eict.common.ApiCaller;
import edu.pucmm.eict.common.IpApiResponse;
import edu.pucmm.eict.common.UAAnalyzer;
import nl.basjes.parse.useragent.UserAgent;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

public class ReferrerService {

    private static ReferrerService instance;
    private final ApiCaller apiCaller;
    private final UAAnalyzer analyzer;
    private final ShortenedUrlDao shortenedUrlDao;
    private final ReferrerDao referrerDao;

    private ReferrerService() {
        shortenedUrlDao = ShortenedUrlDao.getInstance();
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
    public ShortenedUrl newReferrer(String code, String ip, String uaHeader) {
        ShortenedUrl shortenedUrl = shortenedUrlDao.findByCode(code).orElseThrow(EntityNotFoundException::new);

        // Do API calls to get stats
        IpApiResponse geoStats = apiCaller.getGeoStats(ip);

        // Analyze User Agent Header
        UserAgent userAgent = analyzer.parse(uaHeader);
        String platform = userAgent.getValue("DeviceClass");
        String browser = userAgent.getValue("AgentName");
        Referrer referrer = new Referrer(ip, platform, browser, geoStats.getCountry());

        referrer.setShortenedUrl(shortenedUrl);
        referrerDao.create(referrer);

        long increment = shortenedUrl.getVisitCount() + 1;
        shortenedUrl.setVisitCount(increment);
        return shortenedUrlDao.update(shortenedUrl);
    }
}