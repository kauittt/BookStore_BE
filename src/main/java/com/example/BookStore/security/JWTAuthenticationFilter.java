package com.example.BookStore.security;

import com.example.BookStore.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {


    private final JWTGenerator tokenGenerator;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    @Lazy
    public JWTAuthenticationFilter(JWTGenerator tokenGenerator, CustomUserDetailsService customUserDetailsService) {
        this.tokenGenerator = tokenGenerator;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJWTFromRequest(request);
            if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
                String username = tokenGenerator.getUsernameFromJWT(token);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                //- Mục đích: Đoạn mã này tạo ra một đối tượng UsernamePasswordAuthenticationToken (đây là một lớp con của Authentication)
                //- chứa thông tin về người dùng đã xác thực,
                //- bao gồm chi tiết về tên đăng nhập và các quyền của họ.
                //- Vì đã tokenGenerator.validateToken(token) nên không truyền para2 (password)
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());


                //- Mục đích: Dòng mã này được sử dụng để gán các thông tin bổ sung về yêu cầu HTTP hiện tại (request) vào đối tượng UsernamePasswordAuthenticationToken.
                //- Kết quả: Sau dòng này, UsernamePasswordAuthenticationToken không chỉ chứa thông tin về người dùng và quyền hạn của họ,
                //- mà còn có các chi tiết cụ thể liên quan đến yêu cầu HTTP đã khởi tạo quá trình xác thực.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //- Mục đích: Dòng mã này được sử dụng để lưu trữ đối tượng xác thực authenticationToken trong SecurityContext của Spring Security,
                //- giúp hệ thống biết rằng người dùng hiện tại đã được xác thực.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    //- Helper
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //- phương thức shouldNotFilter: Bạn có thể cho phép truy cập tự do (không cần JWT) vào các endpoint như /users/login và /users/register,
    //- để người dùng có thể đăng nhập và đăng ký.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.equals("/users/login") || path.equals("/users/register");
    }
}
