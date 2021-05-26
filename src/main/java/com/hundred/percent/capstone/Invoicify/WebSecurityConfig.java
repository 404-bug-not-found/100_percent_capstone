package com.hundred.percent.capstone.Invoicify;

import com.hundred.percent.capstone.Invoicify.Security.Jwt.JwtAuthenticationFilter;
import com.hundred.percent.capstone.Invoicify.Security.Jwt.JwtAuthorizationFilter;
import com.hundred.percent.capstone.Invoicify.Security.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /*
     * This method can be overridden to set up a wide range of authentication options within spring security.
     * We need to override it so we can inject our own User Details Service.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    /*
     * This is the main method you will override to setup general security in your application. This includes
     * things like CORS, secure routes, custom filters, etc.
     *
     * Note: We have omitted any formLogin configuration here to disable that feature. Since this is a REST API
     * we do not want any form-based authentication.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Add the custom JWT filters to the web filter chain
                // The filter instances are retrieved from the bean definitions below.
                .addFilterAt(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthorizationFilter())

                // Apply our CORs configuration defined below
                .cors()
                .and()

                /*
                 * Set up our secure and non-secure routing
                 * Anything using authenticated() will require the request be authorized before being routed to the controller
                 * permitAll() allows any user to access those resources
                 *
                 * Note: We allow POSTs to /employee so employee can be created without requiring authorization
                 * NOTE: these antMatchers are run sequentially
                 */
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/employee").permitAll()
                .antMatchers("/employee/**").authenticated()

                .antMatchers(HttpMethod.POST, "/companies/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/companies/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/companies/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/companies/**").authenticated()

                .antMatchers(HttpMethod.POST, "/addresses/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/addresses/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/addresses/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/addresses/**").authenticated()

                .antMatchers(HttpMethod.POST, "/invoices/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/invoices/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/invoices/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/invoices/**").authenticated()

                .antMatchers("/**").permitAll()
//				.antMatchers("/employeeName").authenticated()
                .and()

                // Since we are utilizing JSON web tokens we don't want to use cookie-based sessions
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // CSRF security is not needed with a stateless REST API
                .csrf().disable()

                // Allows Spring Security to catch and handle some common exceptions, making HTTP responses for us
                // We also setup a general "Unauthorized" handler to handle unauthorized requests to secure resources
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    /* ------------------------------------------------------------------------
     * BEANS
     * Some beans that we want Spring to manage for us. Some of these are required to be beans so we can autowire
     * dependencies within the classes themselves.
     * -----------------------------------------------------------------------*/

    /*
     * Basic CORS configuration. This is a bean so it can managed by Spring and injected where needed.
     * Note: There are many ways to configure CORS in your security configuration.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Modify these lines for any origins/methods you need to allow CORS for
        configuration.setAllowedOrigins(Arrays.asList("http://someplace.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /*
     * Making this a bean allows us to autowire it in the user service for encrypting passwords when creating users.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * The following objects need to be beans so dependencies can be autowired within them.
     * If Spring does not manage these then autowired dependencies will be not be filled.
     */
    @Bean
    public AbstractAuthenticationProcessingFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(new AntPathRequestMatcher("/authenticate", RequestMethod.POST.name()));
        filter.setAuthenticationManager(authenticationManager());

        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new SecurityService();
    }
}
