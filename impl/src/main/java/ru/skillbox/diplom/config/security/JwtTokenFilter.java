package ru.skillbox.diplom.config.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.skillbox.diplom.service.SocialNetworkService;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtTokenFilter extends OncePerRequestFilter implements SocialNetworkService {

    private static final Class<JwtTokenProvider> loggerClass = JwtTokenProvider.class;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            String jwt =
                    getJwtFromRequest(request);
            if (jwt != null) {
                if (jwtTokenProvider.validateToken(jwt)) {
                    Authentication auth = jwtTokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            log(loggerClass, LoggerLevel.DEBUG, "doFilterInternal", LoggerValue.AUTHENTICATION_FAILED, request.toString());
            //ToDo redirect to login
        }
        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }
}
