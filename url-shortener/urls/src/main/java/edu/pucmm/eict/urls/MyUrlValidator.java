package edu.pucmm.eict.urls;

import org.apache.commons.validator.routines.UrlValidator;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MyUrlValidator {
    private final UrlValidator urlValidator;

    @Inject
    public MyUrlValidator(UrlValidator urlValidator) {
        this.urlValidator = urlValidator;
    }

    public boolean hasSchema(String url) {
        List<String> SCHEMAS = List.of("http://", "https://");
        for(var schema : SCHEMAS) {
            if(url.startsWith(schema)) {
                return true;
            }
        }

        return false;
    }

    public String attachSchema(String url) {
        if(!hasSchema(url)) {
            return "http://" + url;
        }
        return url;
    }

    public boolean isValid(String url) {
        String proxy = attachSchema(url);
        return urlValidator.isValid(proxy);
    }

    public String getHost(String url) {
        String proxy = attachSchema(url);
        try {
            return new URL(proxy).getHost();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
