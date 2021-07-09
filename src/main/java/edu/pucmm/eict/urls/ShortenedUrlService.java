package edu.pucmm.eict.urls;

public class ShortenedUrlService {

    private static ShortenedUrlService instance;

    private ShortenedUrlService() {
    }

    public static ShortenedUrlService getInstance() {
        if (instance == null) {
            instance = new ShortenedUrlService();
        }
        return instance;
    }
}
