package com.haya.autenticate_service.filter;

import com.haya.autenticate_service.security.service.MyUserDetailsService;
import com.haya.autenticate_service.security.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class AuthFilterToken extends OncePerRequestFilter {

    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtUtil;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String headerAuth = request.getHeader("Authorization");

        String token = null;
        String username = null;
        if(headerAuth != null && headerAuth.startsWith("Bearer ")){
            token = headerAuth.substring(7);
            username = jwtUtil.extraerUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            if (jwtUtil.validarToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken userPassAuthToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                userPassAuthToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
            }
        }

        filterChain.doFilter(request, response);
    }


}
