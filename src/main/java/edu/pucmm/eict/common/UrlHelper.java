package edu.pucmm.eict.common;

import org.apache.commons.validator.routines.UrlValidator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class UrlHelper {

    private static UrlHelper instance;
    private final UrlValidator validator;

    private UrlHelper() {
        validator = new UrlValidator();
    }

    public static UrlHelper getInstance() {
        if (instance == null) {
            instance = new UrlHelper();
        }
        return instance;
    }

    public boolean hasUrlPrefix(String url) {
        List<String> protocols = List.of("http://", "https://");
        for(var protocol : protocols) {
            if(url.startsWith(protocol)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid(String url) {
        String proxy = url;
        if(!hasUrlPrefix(url)) {
            proxy = "http://" + url;
        }
        return validator.isValid(proxy);
    }

    public String getHost(String url) {
        String proxy = url;
        if(!hasUrlPrefix(proxy)) {
            proxy = "http://" + url;
        }
        try {
            return new URL(proxy).getHost();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}