package edu.pucmm.eict.urls;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

public class UrlPreviewerImpl implements UrlPreviewer {

    private final String NO_IMAGE = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARgAAAC0CAMAAAB4+cOfAAAAPFBMVEXq6uqurq7X19fi4uKqqqrp6ene3t6vr6+0" +
            "tLTHx8fl5eW3t7fd3d3t7e3U1NTZ2dm9vb3MzMzCwsKlpaXgX8HoAAAEWElEQVR4nO3a626rOhAFYOPBd3s8dt//Xc+YJLupdqV0/zilFetT2xAKCK8MJgaMAQAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA+F858nd09q78LFn2u+DO3pcfpLUs211w7VNn7+MJGo00459g0qeGP3s3vx/HsL0W+ez9/G41vk5lKVerGXs7gl4n08/e029md60G5vk" +
            "xmlBE5EMt7fZiHbAGs9fn89KKZXRPRJXnxYPRJrenDCa5Zo5TtOvxPZiz9/SbrWC0zVT+5DLWdzxHlY7XcuGK2bY0nnLRmV67nLDFoQOEXK4czBPJ60xVEndOUaqezwOCWbi1Hibla" +
            "qvzojG5dNk+5sN5eo0oi2OJMYqlogeWDagYPZKaBkHpnhJzbMYXBKPdcGtD+DEvcMiGBMHoOUmDeR9rbymQyYI+5lYxT0OBuDlUzKOP6c9jpNJMLagY7VX0a8zzCImN4Q0Vc3Qyz8OD" +
            "5O5dDIKJ3jSS27fdmPKjYBDMJjpAcjxFJHVn2qPHuXofs5LxzTRHREZrxD56YlSMnomGud80oXTp6zF/i8l68ixP1ztRMfcclg8zrlYx/St3lZar3SUgeZ3JIpe73V/ntr8U5tXut5l" +
            "13fsL8BAEAADA6dzXz8dr0ffF71P/sP7pcq3OuK89oJrZfrVljjnTeGyVxjFUyOMXjRhsidxIklnPZb7Pfp98ehgzD3YfF/t7mfsbxyPbzd4fFKlxrBfaUvt0/R+ocdyip6LB9MF07H" +
            "Puvo/aXCfSv6xpOEuu9Zp7Ncbz6I06GerZ+WM0TTxGdWte7pT1jTWudteDdX2srWowXauF1g0pGr+ibhoH2WfVihmhxHCMdXyIEgORzhAjsYSSZeYc0iqsvhazq4kpchZZJTR0+WhXWf" +
            "DW+6Yrs0lCGkyWIkFyLUFn9rVWj6WEcXazX9Ng+txmSXlPLUdZJVM3bj4m2lMl1jaOjUfwHOoKJkrOJTqZPkbRLNZGstPlkisxz5JdJi7FzCMY7W6rxOo1M4qphqFJZY3z5w/CNRi7nodK" +
            "K41W4i2Y1YxJu84ZWylFtGUsxaxgdv3ERdooWjAl3UpMg5CQDIdRhvHrjdyDyTNKKX4Fo1WnwZAE/X/6FcFws2FLWX/8o2KG00YewfA2vLfezbLSWhVTuq/riIjTac1ks54IiVRjclmPst" +
            "pm7L7cg+l9Hy7FFYyzIXnd8CyWqs1nt/slbTk3l/ZkUpASb33Mpp95yf5ttNVokWh1sT0bKlPPYkWXa1TerJlv6XgCL20yt+Sa9lZZa0xm0EOpkN3tOt70UKqxaF/l9eBsx/rz51eMyT6vP" +
            "6TnEebb/tYtrUnnj3KoPLwzmfxxTUZ/me16WTP97ZPPlml9E1qbMa6z1+WInG7Aea5risjet+h0in/phZvVx5y9Dz9Sthe8QgkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "p/gPjOMyqC28HqMAAAAASUVORK5CYII=";
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0";
    private final String REFERRER = "https://www.google.com/";
    private final String PREFIX = "data:image/png;base64,";

    public byte[] recoverImageFromUrl(String urlText) throws Exception {
        URL url = new URL(urlText);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte[] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }
        return output.toByteArray();
    }

    @Override
    public String getPreviewImg(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent(USER_AGENT).referrer(REFERRER).get();
            Element image = doc.select("meta[property=og:image]").first();
            if(image != null) {
                String previewLink = image.attr("content");
                var imageBytes = recoverImageFromUrl(previewLink);
                return PREFIX + Base64.getEncoder().encodeToString(imageBytes);
            }
        } catch (Exception e) {
            return NO_IMAGE;
        }
        return NO_IMAGE;
    }
}
