//package com.example.BookStore.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.crypto.SecretKey;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class JWTTokenValidatorFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
//
//        if (jwt != null) {
//            try {
//                SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
//                Claims claims = Jwts.parser()
//                        .setSigningKey(key)
//                        .build()
//                        .parseClaimsJws(jwt)
//                        .getBody();
//
//                String username = claims.get("username", String.class);
//                String authorities = claims.get("authorities", String.class);
//
//                Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
//                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
//
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            } catch (Exception e) {
//                throw new BadCredentialsException("Invalid Token received!", e);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
////    @Override
////    protected boolean shouldNotFilter(HttpServletRequest request) {
//////        return request.getServletPath().equals("/user");
////        String path = request.getServletPath();
////        return path.equals("/users/register") || path.equals("/login");
////    }
//}
