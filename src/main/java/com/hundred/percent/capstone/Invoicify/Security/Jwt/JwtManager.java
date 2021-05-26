package com.hundred.percent.capstone.Invoicify.Security.Jwt;

import com.hundred.percent.capstone.Invoicify.Security.SecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * This is a helper class that does the heavy lifting of
 * creating and parsing the actual JSON web tokens. JJWT is utilized
 * to make the process easier.
 */
@Service
public class JwtManager {

    /*
     * Some statics that help create consistent implementation and testing.
     */
    public static final long EXPIRATION_MODIFIER = 1000 * 60 * 60 * 10;

    public static final String JWT_HEADER = "Authorization";

    public static final String JWT_PREFIX = "Bearer ";

    public static SecretKey KEY;

    /*
     * This sets up the key used to sign the token. An configuration variable is used from
     * the application.yml to allow tokens to stay valid even if the application restarts.
     * Note: This is not the only way to get a variable from the application.yml.
     */
    @Value("${jwt-secret}")
    public void setKey(String secret) {
        KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /*
     * For more information on what exactly is going on here, see the JJWT docs
     * and general information on JSON web tokens. Basically this builds a JSON object
     * then returns the signed string.
     */
    private String create(String subject, Map<String, Object> claims) {
        long currentTime = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(currentTime + EXPIRATION_MODIFIER))
                .signWith(KEY)
                .compact();
    }

    /*
     * Returns a token filled with user information.
     * The user Id is used as the subject of the token.
     */
    public JwtToken generateToken(SecurityUser user) {
        String tokenString = create(user.getId().toString(), new HashMap<>());

        return new JwtToken(tokenString);
    }

    /*
     * Parses a JSON web token string into a set of claim objects. Basically returns the raw data inside the token.
     */
    private Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);
    }

    /*
     * Returns a JwtToken object filled with the data from the given token string.
     */
    public JwtToken getToken(String token) {
        try {
            return new JwtToken(token, parseToken(token));
        } catch (JwtException e) {
            // An empty JwtToken will be invalid and the subject will be null.
            return new JwtToken();
        }
    }
}
