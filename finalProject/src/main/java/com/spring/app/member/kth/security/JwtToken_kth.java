package com.spring.app.member.kth.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// 로그인 성공 시 클라이언트에게 발급해 줄 토큰 DTO
@Data
@AllArgsConstructor
@Builder
public class JwtToken_kth {

    // 토큰 인증 방식 (Bearer)
    private String grantType;

    // 실제 인증에 사용되는 JWT 문자열
    private String accessToken;

    // Access Token의 만료 시간
    private Long accessTokenExpiresIn;

    // Access Token이 만료되었을 때 재발급을 위한 토큰
    private String refreshToken;

}