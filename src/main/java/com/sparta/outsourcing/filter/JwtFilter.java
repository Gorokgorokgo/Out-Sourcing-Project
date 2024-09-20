package com.sparta.outsourcing.filter;


import com.sparta.outsourcing.entity.UserRoleEnum;
import com.sparta.outsourcing.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;


import java.io.IOException;

@Slf4j
@Order(1)
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        // 특정 URL & HTTP 메서드에 대해 필터를 건너뛰도록 설정
        if (url.equals("/api/users/signup") || url.equals("/api/users/login")) {
            chain.doFilter(request, response);
            return;
        }

        String tokenValue = httpRequest.getHeader("Authorization"); //key값

        if (tokenValue == null || !tokenValue.startsWith("Bearer ")) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요합니다.");
            return;
        }

        String token = jwtUtil.substringToken(tokenValue);

        try {

            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.getUserInfoFromToken(token);

            // 사용자 정보를 ArgumentResolver 로 넘기기 위해 HttpServletRequest 에 세팅
            String roleString = claims.get("role", String.class);
            UserRoleEnum role = UserRoleEnum.valueOf(roleString);  // 문자열을 Enum으로 변환


            httpRequest.setAttribute("customerId", claims.getSubject());
            httpRequest.setAttribute("role", role);
            httpRequest.setAttribute("email", claims.get("email", String.class));


            if (jwtUtil.validateToken(token)) {
                chain.doFilter(request, response);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
            }
        } catch (Exception e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰 검증 중 오류가 발생했습니다.");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}