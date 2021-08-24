package edu.pucmm.eict.restapi.config;

import io.javalin.http.Context;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
    private final String secret;
    private final Integer tokenExpirationMs;

    @Inject
    public JwtUtils(@Named("jwtSecret") String secret, @Named("jwtExpiration") Integer tokenExpirationMs) {
        this.secret = secret;
        this.tokenExpirationMs = tokenExpirationMs;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuer("Wolfisc - Simple Url Shortener")
                .setSubject("Jwt Token")
                .setExpiration(new Date((new Date()).getTime() + tokenExpirationMs))
                .claim("user", username)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUserFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .get("user", String.class);
    }

    public boolean validateJwtToken(String authToken) throws UnsupportedJwtException {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String parseJwt(Context ctx) {
        String headerAuth = ctx.header("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}