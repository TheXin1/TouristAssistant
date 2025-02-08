package org.datateam.touristassistant.Filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.datateam.touristassistant.utils.JwtUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Order(1)
@WebFilter(filterName = "myFilter1",urlPatterns = {"/api/*"})
public class JwtAuthenticationFilter implements Filter {

    private JwtUtil jwtUtil = new JwtUtil();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 获取 Authorization 头部中的 Token
        String token = httpRequest.getHeader("Authorization");

        // 如果没有 token 或 token 无效
        if (token == null || token.isEmpty() || !isValidToken(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置 401 状态码
            httpResponse.getWriter().write("Token无效请重新登录");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    // 检查 Token 是否有效
    private boolean isValidToken(String token) {
        try {
            // 检查 JWT
            if (jwtUtil.isTokenExpiration(token)) {
                return false; // Token 已过期
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

