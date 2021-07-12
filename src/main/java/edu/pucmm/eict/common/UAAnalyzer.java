package edu.pucmm.eict.common;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class UAAnalyzer {

    private static UAAnalyzer instance;
    private final UserAgentAnalyzer uaa;


    private UAAnalyzer() {
        this.uaa = UserAgentAnalyzer.newBuilder()
                .withCache(1234)
                .withField("DeviceClass")
                .withAllFields()
                .build();
    }

    public static UAAnalyzer getInstance() {
        if (instance == null) {
            instance = new UAAnalyzer();
        }
        return instance;
    }

    public UserAgent parse(String uaHeader) {
        return uaa.parse(uaHeader);
    }
}