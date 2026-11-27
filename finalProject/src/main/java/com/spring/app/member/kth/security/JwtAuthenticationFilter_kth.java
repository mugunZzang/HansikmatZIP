package com.spring.app.member.kth.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 요청에 포함된 JWT 토큰을 검증하고 인증정보를 설정하는 필터
@Component
public class JwtAuthenticationFilter_kth extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider_kth jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 헤더에서 JWT 토큰 가져오기
        String token = resolveToken(request);


        // JWT 토큰이 존재하고 유효한 경우
        if (SecurityContextHolder.getContext().getAuthentication() == null
                && token != null
                && jwtTokenProvider.validateToken(token)) {

            try {

                // JWT 토큰에서 인증정보 가져오기
                Authentication authentication =
                        jwtTokenProvider.getAuthentication(token);

                // Spring Security에 인증정보 저장하기
                SecurityContextHolder.getContext()
                                     .setAuthentication(authentication);

            } catch (JwtException | IllegalArgumentException e) {

                SecurityContextHolder.clearContext();

            }

        }

        // 다음 필터로 요청 전달하기
        filterChain.doFilter(request, response);
    }


    // 요청 헤더에서 JWT 토큰 추출하기
    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

            return bearerToken.substring(7);

        }

        return null;
    }

}