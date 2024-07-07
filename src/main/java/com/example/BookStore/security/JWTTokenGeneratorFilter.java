//package com.example.BookStore.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.crypto.SecretKey;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Collection;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//@Component
//public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request
//            , @NonNull HttpServletResponse response
//            , @NonNull FilterChain filterChain)
//            throws ServletException, IOException {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null) {
//            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
//            String jwt = Jwts.builder()
//                    .setIssuer("Book Store")
//                    .setSubject("JWT Token")
//                    .claim("username", authentication.getName())
//                    .claim("authorities", populateAuthorities(authentication.getAuthorities()))
//                    .setIssuedAt(new Date())
//                    .setExpiration(new Date((new Date()).getTime() + 30000000)) // Adjust expiration as needed
//                    .signWith(key)
//                    .compact();
//
//            response.setHeader(SecurityConstants.JWT_HEADER, jwt);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    //!...........
////    @Override
////    protected boolean shouldNotFilter(HttpServletRequest request) {
//////        return !request.getServletPath().equals("/user");
////        String path = request.getServletPath();
////        return path.equals("/users/register") || path.equals("/login");
////    }
//
//    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
//        return collection.stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//    }
//
//
//}
