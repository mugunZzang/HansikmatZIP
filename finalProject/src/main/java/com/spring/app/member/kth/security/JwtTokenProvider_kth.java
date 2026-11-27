package com.spring.app.member.kth.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.spring.app.member.kth.domain.CustomUserDetails_kth;
import com.spring.app.member.kth.domain.MemberDTO_kth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import lombok.extern.slf4j.Slf4j;

// JWT 생성, 검증 및 인증정보 추출을 담당하는 클래스
@Component
@Slf4j
public class JwtTokenProvider_kth {

    private static final String AUTHORITIES_KEY = "auth";

    private static final String BEARER_TYPE = "Bearer";

    // Access Token 유효기간 : 30분
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    // Refresh Token 유효기간 : 7일
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

    private final SecretKey secretKey;


    // 생성자
    public JwtTokenProvider_kth(@Value("${jwt.secret}") String secretKey) {

        // application.yml에서 JWT 비밀키 가져오기
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    // 인증된 사용자 정보를 가지고 JWT 토큰 생성하기
    public JwtToken_kth generateToken(Authentication authentication) {

        // 사용자의 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();

        Date accessTokenExpiresIn =
                new Date(now + ACCESS_TOKEN_EXPIRE_TIME);


        // Access Token 생성하기
        String accessToken = Jwts.builder()
                .subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", "access")
                .issuedAt(new Date(now))
                .expiration(accessTokenExpiresIn)
                .signWith(secretKey)
                .compact();


        // Refresh Token 생성하기
        String refreshToken = Jwts.builder()
                .subject(authentication.getName())
                .claim("type", "refresh")
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date(now))
                .expiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(secretKey)
                .compact();


        // 생성한 JWT 토큰 반환하기
        return JwtToken_kth.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }


    // Access Token에서 인증정보 가져오기
    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        // Access Token 및 권한 정보 확인
        if (!"access".equals(claims.get("type"))
                || claims.get(AUTHORITIES_KEY) == null) {
            throw new JwtException("유효한 인증용 토큰이 아닙니다.");
        }


        // 토큰에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                      .map(SimpleGrantedAuthority::new)
                      .collect(Collectors.toList());


        // 토큰에서 회원 아이디 가져오기
        String userid = claims.getSubject();


        // MemberDTO_kth 생성하기
        MemberDTO_kth memberDto = new MemberDTO_kth();

        memberDto.setUserid(userid);


        // 회원 권한 설정하기
        memberDto.setAuthorities(
                authorities.stream()
                           .map(GrantedAuthority::getAuthority)
                           .collect(Collectors.toList())
        );


        // CustomUserDetails_kth 생성하기
        CustomUserDetails_kth principal =
                new CustomUserDetails_kth(memberDto);


        return new UsernamePasswordAuthenticationToken(
                principal,
                "",
                principal.getAuthorities()
        );
    }


    // JWT 토큰의 유효성 검증하기
    public boolean validateToken(String token) {

        try {

            parseClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {

            log.info("유효하지 않은 JWT 토큰입니다.");

        }

        return false;
    }


    // JWT 토큰에서 Claims 추출하기
    private Claims parseClaims(String token) {

        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }
    
 // Refresh Token을 SHA-256 해시값으로 변환하기
    public String hashRefreshToken(String refreshToken) {

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(
                    refreshToken.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        }
        catch(NoSuchAlgorithmException e) {

            throw new IllegalStateException("Refresh Token 해시 처리 실패", e);

        }
    }
    
    // Refresh Token 유효기간 반환하기 (초 단위)
    public long getRefreshTokenExpireTime() {

        return REFRESH_TOKEN_EXPIRE_TIME / 1000;
    }

    
   // Refresh Token에서 회원 아이디 가져오기
    public String getUseridFromRefreshToken(String refreshToken) {

        Claims claims = parseClaims(refreshToken);

        // Refresh Token인지 확인하기
        if(!"refresh".equals(claims.get("type"))) {

            throw new JwtException("유효한 Refresh Token이 아닙니다.");

        }

        return claims.getSubject();
    }
}