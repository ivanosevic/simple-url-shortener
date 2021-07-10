package edu.pucmm.eict.urls.dao;

import edu.pucmm.eict.common.Dao;
import edu.pucmm.eict.urls.models.Referrer;

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