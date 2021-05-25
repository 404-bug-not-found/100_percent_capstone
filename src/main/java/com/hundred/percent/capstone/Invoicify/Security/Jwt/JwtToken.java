package com.hundred.percent.capstone.Invoicify.Security.Jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Getter;

import java.util.Date;

/*
 * This is a data object with a couple helper methods used in our JSON web token implementation.
 * The implementation of this object may change as the project develops or better designs are found.
 */
public class JwtToken {

    /*
     * The token is the only value that will be serialized to JSON.
     * Every other value in the object is ignored. This allows this object
     * to be used for both sending in a response (new tokens) and for use in
     * the authorization process (validation and data gathering). This also
     * allows control of what values can be accessed from the token itself.
     */
    @Getter
    private String token = "";

    private Claims body = null;

    public JwtToken() {
    }

    /*
     * The string constructor is for serializing a new JWT response,
     * the other constructor is used for managing parsed tokens.
     */
    public JwtToken(String tokenString) {
        this.token = tokenString;
    }

    public JwtToken(String tokenString, Jws<Claims> claims) {
        this.token = tokenString;
        this.body = claims.getBody();
    }

    @JsonIgnore
    public String getSubject() {
        if (body == null) {
            return null;
        }

        return body.getSubject();
    }

    /*
     * A token is valid if now is before the expiration.
     */
    @JsonIgnore
    public boolean isValid() {
        if (body == null) {
            return false;
        }

        return body.getExpiration().after(new Date());
    }
}
