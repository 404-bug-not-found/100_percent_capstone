package com.hundred.percent.capstone.Invoicify.Security.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundred.percent.capstone.Invoicify.Security.SecurityUser;
import com.hundred.percent.capstone.Invoicify.utilities.ExcludeGeneratedFromJaCoCo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/*
 * This filter is used to take in JSON for authentication and then send a JSON web token when
 * authentication is successful.
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private JwtManager jwtManager;

  /*
   * Providing a RequestMatcher constructor allows us to specify a route on that will
   * have this filter applied when instantiated.
   */
  public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
    super(requiresAuthenticationRequestMatcher);
  }

  /*
   * The logic here allows you to send the username and password via JSON instead of the default
   * form data format.
   */
  @ExcludeGeneratedFromJaCoCo
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    UsernamePasswordAuthenticationToken authRequest;

    try (InputStream body = request.getInputStream()) {
      Map<String, Object> jsonBody = objectMapper.readValue(body, Map.class);
      authRequest = new UsernamePasswordAuthenticationToken(jsonBody.get("employeeName"), jsonBody.get("password"));
    } catch (IOException e) {
      // If there is an exception, print it then create a failing (empty) authentication object
      e.printStackTrace();
      authRequest = new UsernamePasswordAuthenticationToken("", "");
    }

    return getAuthenticationManager().authenticate(authRequest);
  }

  /*
   * Return a JSON web token on successful authentication.
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, AuthenticationException {
    response.setStatus(200);
    response.setContentType("application/json;charset=UTF-8");

    JwtToken jwt = jwtManager.generateToken((SecurityUser) authentication.getPrincipal());

    response
        .getWriter()
        .write(objectMapper.writeValueAsString(jwt));
  }

  /*
   * This override helps provide better error feedback when authentication fails.
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json;charset=UTF-8");

    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", Calendar.getInstance().getTime());
    body.put("error", exception.getMessage());

    response
        .getOutputStream()
        .println(objectMapper.writeValueAsString(body));
  }
}
