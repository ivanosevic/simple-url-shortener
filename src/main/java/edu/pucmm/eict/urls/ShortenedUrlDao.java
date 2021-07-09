package edu.pucmm.eict.urls;

import edu.pucmm.eict.common.Dao;

public class ShortenedUrlDao extends Dao<ShortenedUrl, Long> {

    private static ShortenedUrlDao instance;

    private ShortenedUrlDao() {
        super(ShortenedUrl.class);
    }

    public static ShortenedUrlDao getInstance() {
        if (instance == null) {
            instance = new ShortenedUrlDao();
        }
        return instance;
    }
}