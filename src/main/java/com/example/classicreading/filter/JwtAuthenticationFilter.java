package com.example.classicreading.filter;

import com.example.classicreading.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        // 如果没有 token 或不是 Bearer 开头，直接放行，由 SecurityConfig 决定是否允许访问
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(jwt);

            // ===================== 新增：JWT 解析日志 =====================
            System.out.println("【JWT调试】解析得到用户名：" + username);
            // =============================================================

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // ===================== 新增：认证成功日志 =====================
                    System.out.println("【JWT调试】用户认证成功：" + username);
                    // =============================================================
                }
            }
        } catch (Exception e) {
            // ===================== 新增：解析失败日志 =====================
            System.out.println("【JWT调试】解析失败：" + e.getMessage());
            // =============================================================

            // Token 无效或过期时不抛 500，交由后续鉴权流程返回 401
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }
}