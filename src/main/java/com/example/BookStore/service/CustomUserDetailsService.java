package com.example.BookStore.service;

import com.example.BookStore.model.Authority;
import com.example.BookStore.model.User;
import com.example.BookStore.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final AuthorityService authorityService;

    @Autowired
    public CustomUserDetailsService(AuthorityService authorityService, UserService userService) {
        this.authorityService = authorityService;
        this.userService = userService;
    }

    //- Việc triển khai các interface GrantedAuthority trong lớp Authority và UserDetails trong lớp UserPrincipal
    //- giúp cho việc sử dụng CustomUserDetailsService dễ dàng hơn và đồng thời tuân thủ đúng các yêu cầu của Spring Security.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        //- load Authorities
        List<Authority> authorities = authorityService.getAllAuthoritiesByUsername(username);
        user.setAuthorities(Objects.requireNonNullElseGet(authorities, List::of));

        return new UserPrincipal(user);
    }
}


