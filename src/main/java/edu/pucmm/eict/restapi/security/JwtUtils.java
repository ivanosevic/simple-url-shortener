package edu.pucmm.eict.restapi.security;

import edu.pucmm.eict.common.ApplicationProperties;
import io.javalin.http.Context;
import io.jsonwebtoken.*;
import nl.basjes.shaded.org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final String secret;
    private final Integer jwtExpirationMs;

    private static class InstanceHolder {
        public static JwtUtils instance = new JwtUtils();
    }

    private JwtUtils() {
        secret = ApplicationProperties.getInstance().getRestapiSecret();
        jwtExpirationMs = ApplicationProperties.getInstance().getRestapiExpirationMs();
    }

    public static JwtUtils getInstance() {
        return InstanceHolder.instance;
    }

    public JwtUtils(String secret, Integer jwtExpirationMs) {
        this.secret = secret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String parseJwt(Context ctx) {
        String headerAuth = ctx.header("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}
