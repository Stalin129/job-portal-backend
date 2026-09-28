package com.example.Job_Portal.jwtAuth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtAuth jwtAuth;

    public JwtAuthFilter(JwtAuth jwtAuth) {
        this.jwtAuth = jwtAuth;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {

        String token = authHeader.substring(7);
        String roles = jwtAuth.extractToken(token);

        if (roles != null) {

            List<SimpleGrantedAuthority> authorities =
                    List.of(
                            new SimpleGrantedAuthority("ROLE_" + roles.toUpperCase())
                    );
            UsernamePasswordAuthenticationToken authenticated =
                    new UsernamePasswordAuthenticationToken(
                            roles, null, authorities
                    );
            SecurityContextHolder.getContext().setAuthentication(authenticated);
        }
        filterChain.doFilter(
                request, response
        );


    }
    catch (
    ExpiredJwtException e) {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("TOKEN_EXPIRED");
        return;

    } catch (Exception e) {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("INVALID_TOKEN");
        return;
    }}
}
