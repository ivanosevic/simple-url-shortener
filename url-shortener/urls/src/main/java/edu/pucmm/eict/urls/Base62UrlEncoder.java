package edu.pucmm.eict.urls;

import io.seruco.encoding.base62.Base62;

public class Base62UrlEncoder implements UrlEncoder {
    private final Base62 base62Encoder;

    public Base62UrlEncoder(Base62 base62Encoder) {
        this.base62Encoder = base62Encoder;
    }

    @Override
    public String getCode(String id) {
        final byte[] encoded = base62Encoder.encode(id.getBytes());
        return new String(encoded);
    }
}
