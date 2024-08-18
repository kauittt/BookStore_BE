package com.example.BookStore.config;

import com.example.BookStore.security.JWTAuthenticationFilter;
import com.example.BookStore.security.JWTGenerator;
import com.example.BookStore.security.JwtAuthEntryPoint;
import com.example.BookStore.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class BookStoreConfig {
    private final JwtAuthEntryPoint authEntryPoint;
    private final JWTGenerator jwtGenerator;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    @Lazy
    public BookStoreConfig(JwtAuthEntryPoint authEntryPoint, JWTGenerator jwtGenerator, CustomUserDetailsService customUserDetailsService) {
        this.authEntryPoint = authEntryPoint;
        this.jwtGenerator = jwtGenerator;
        this.customUserDetailsService = customUserDetailsService;
    }

    //- là một trong những AuthenticationProvider
    //- được sử dụng phổ biến trong Spring Security để xác thực người dùng dựa trên username và password.
    //- có thể sử dụng CustomUserDetailsService thay vì userDetailsService cơ bản trong phương thức authenticationProvider().
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    //- Import bên UserController, để gọi .authenticate khi login, gọi cái "Dao" ở trên
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorize -> {
                    //- Đảm bảo rằng các endpoint này có thể truy cập mà không cần xác thực.
                    authorize.requestMatchers("/users/login", "/users/register", "/users/refresh").permitAll();

                    //- Test author
                    authorize.requestMatchers(HttpMethod.PUT, "/books/{id}").hasRole("ADMIN");

                    //- Yêu cầu tất cả các request khác phải được xác thực,
                    //- tức là người dùng phải có thông tin trong SecurityContext (thông qua JWT hoặc cách xác thực khác).
                    authorize.anyRequest().authenticated();
                });

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //- Phương thức này tạo và cấu hình một instance của JWTAuthenticationFilter.
    //- JWTAuthenticationFilter là một filter tùy chỉnh dùng để xử lý xác thực bằng cách kiểm tra JWT trong các request HTTP.
    //- filter này sẽ được sử dụng để lọc các request ngay từ đầu trong quá trình xử lý request của ứng dụng.
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(jwtGenerator, customUserDetailsService);
    }
}