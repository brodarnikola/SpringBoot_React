package com.example.polls.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;

//  one way how we can handle, protect us from csrf attacks if we have session based authentication
//   this needs to be handled on backend side and also on frontend size ..
//   Backend needs to add correct also to SecurityConfig class, which has annotation @EnableWebSecurity
//   FE side then needs to use X-XSRF token
public class SpaWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if (user != null && !path.startsWith("/api") && !path.contains(".") && path.matches("/(.*)")) {
            request.getRequestDispatcher("/").forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
