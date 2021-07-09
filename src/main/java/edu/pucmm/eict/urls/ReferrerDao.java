package edu.pucmm.eict.urls;

import edu.pucmm.eict.common.Dao;

public class ReferrerDao extends Dao<Referrer, Long> {

    private static ReferrerDao instance;

    private ReferrerDao() {
        super(Referrer.class);
    }

    public static ReferrerDao getInstance() {
        if (instance == null) {
            instance = new ReferrerDao();
        }
        return instance;
    }
}