package com.management.Accounts.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//
//        String path = request.getServletPath();
//
//        return path.startsWith("/api/auth");
//    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Allow CORS preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // 2. Token illai enraal (Anonymous User), filter chain-ai thodara vidungal
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                // Spring Security 403 Error adikkaamal irukka Anonymous Authentication set seigirom
                AnonymousAuthenticationToken anonymousToken = new AnonymousAuthenticationToken(
                        "key",
                        "anonymousUser",
                        AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
                );
                SecurityContextHolder.getContext().setAuthentication(anonymousToken);
            }
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // 3. Token irundhu athu thavaraaga/expire aahi irundhaal 401 adikkum
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.extractUsername(token);

        // 4. Valid token-kaana user authentication set seigirom
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    }

