//package com.example.BookStore.config;
//
//import com.example.BookStore.model.User;
//import com.example.BookStore.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//    private final PasswordEncoder passwordEncoder;
//    private final UserService userService;
//    private final JdbcUserDetailsManager userDetailsManager;
//
//    @Autowired
//    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder, UserService userService, JdbcUserDetailsManager userDetailsManager) {
//        this.passwordEncoder = passwordEncoder;
//        this.userService = userService;
//        this.userDetailsManager = userDetailsManager;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = (String) authentication.getCredentials();
//
//        User user;
//        try {
//            user = userService.getUserByUsername(username);
//        } catch (UsernameNotFoundException e) {
//            throw new AuthenticationException("User not found") {
//            };
//        }
//
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//        } else {
//            throw new AuthenticationException("Invalid credentials") {
//            };
//        }
//    }
//
////    @Override
////    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
////        String username = authentication.getName();
////        String password = (String) authentication.getCredentials();
////
////        UserDetails user = userDetailsManager.loadUserByUsername(username);
////        if (user == null) {
////            throw new UsernameNotFoundException("User not found");
////        }
////
////        if (passwordEncoder.matches(password, user.getPassword())) {
////            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
////        } else {
////            throw new AuthenticationException("Invalid credentials") {};
////        }
////    }
//
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
