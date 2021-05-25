package com.hundred.percent.capstone.Invoicify.Security.Jwt;

import com.hundred.percent.capstone.Invoicify.utilities.ExcludeGeneratedFromJaCoCo;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.hundred.percent.capstone.Invoicify.Security.Jwt.JwtManager.JWT_HEADER;
import static com.hundred.percent.capstone.Invoicify.Security.Jwt.JwtManager.JWT_PREFIX;

/*
 * This filter is used to authorize requests with a JSON web token. If a JWT is not found, no authorization is
 * attempted.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtManager jwtManager;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(JWT_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith(JWT_PREFIX)) {
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(authorizationHeader));
        }

        chain.doFilter(request, response);
    }


    /*
     * This is utilizes the token manager to parse the token, then sets up the authentication object if able.
     * Returning null from this method will cause authorization to fail.
     */
    @ExcludeGeneratedFromJaCoCo
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            JwtToken jwt = jwtManager.getToken(token.replace(JWT_PREFIX, ""));

            System.out.println("JWT Valid? " + jwt.isValid());

            if (!jwt.isValid()) {
                return null;
            }

            return new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, new ArrayList<>());
        } catch (JwtException e) {
            return null;
        }
    }
}
