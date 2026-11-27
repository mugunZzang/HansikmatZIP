package com.spring.app.member.kth.service;

import com.spring.app.common.AES256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import java.security.SecureRandom;

import com.spring.app.member.kth.domain.MemberDTO_kth;
import com.spring.app.member.kth.mail.GoogleMail_kth;
import com.spring.app.member.kth.model.MemberDAO_kth;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.spring.app.member.kth.security.JwtToken_kth;
import com.spring.app.member.kth.security.JwtTokenProvider_kth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;

import com.spring.app.member.kth.domain.CustomUserDetails_kth;

import org.springframework.security.authentication.BadCredentialsException;
import io.jsonwebtoken.JwtException;

@Service
public class MemberService_imple_kth implements MemberService_kth {

	
	
	@Autowired
	private AES256 aes256;
	
    @Autowired
    private MemberDAO_kth dao;
    
    @Autowired
    private GoogleMail_kth googleMail;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider_kth jwtTokenProvider;

    // 아이디 중복검사
    @Override
    public int idDuplicateCheck(String userid) {
        int n = dao.idDuplicateCheck(userid);
        return n;
    }

    // 닉네임 중복검사
    @Override
    public int nicknameDuplicateCheck(String nickname) {
        int n = dao.nicknameDuplicateCheck(nickname);
        return n;
    }

    // 이메일 중복검사
    @Override
    public int emailDuplicateCheck(String email) {

        int n = 0;

        try {
            n = dao.emailDuplicateCheck(aes256.encrypt(email));
        }
        catch(Exception e) {
            throw new IllegalStateException("이메일 중복검사 실패", e);
        }

        return n;
    }

   // 이메일 인증번호 생성 및 발송
    @Override
    public String sendEmail(String email) {

        // 랜덤한 6자리 인증번호 생성하기
        SecureRandom rnd = new SecureRandom();

        String certificationCode = "";

        for(int i = 0; i < 6; i++) {
            certificationCode += rnd.nextInt(10);
        }

        // 이메일 발송하기
        googleMail.sendMail(email, certificationCode);

        return certificationCode;
    }

 // 회원가입 및 권한 부여
    @Override
    @Transactional(propagation=Propagation.REQUIRED,
                   isolation=Isolation.READ_COMMITTED,
                   rollbackFor={Throwable.class})
    public int insertMember(MemberDTO_kth mbrdto) throws Exception {

        int result = 0;

        // 이메일 양방향 암호화 하기
        mbrdto.setEmail(aes256.encrypt(mbrdto.getEmail()));

        // 휴대폰번호 양방향 암호화 하기
        mbrdto.setMobile(aes256.encrypt(mbrdto.getMobile()));

        // tbl_user 테이블에 저장하기
        int n1 = dao.insertMember(mbrdto);

        // tbl_authorities 테이블에 저장하기
        if(n1 == 1) {
            result = dao.insertAuthority(mbrdto.getUserseq());
        }

        if(result != 1) {
            throw new Exception("회원가입 실패");
        }

        return result;
    }

 // JWT 로그인
    @Override
    public JwtToken_kth login(String userid, String passwd,
                              HttpServletRequest request,
                              HttpServletResponse response) {

        // 1. 아이디와 비밀번호를 가지고 인증 객체 생성하기
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userid, passwd);

        // 2. 아이디와 비밀번호가 올바른지 인증하기
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        // 3. 인증된 회원정보를 가지고 JWT 토큰 생성하기
        JwtToken_kth jwtToken =
                jwtTokenProvider.generateToken(authentication);

        // 4. 인증된 회원의 회원번호 가져오기
        CustomUserDetails_kth principal =
                (CustomUserDetails_kth) authentication.getPrincipal();

        long userseq = principal.getUserseq();

        // 5. Refresh Token 해시 처리하기
        String hashedRefreshToken =
                jwtTokenProvider.hashRefreshToken(jwtToken.getRefreshToken());

        // 6. Refresh Token 해시값을 DB에 저장하기
        Map<String, Object> paraMap = new HashMap<>();

        paraMap.put("userseq", userseq);
        paraMap.put("rt_value", hashedRefreshToken);

        int n = dao.saveRefreshToken(paraMap);

        if(n != 1) {
            throw new IllegalStateException("Refresh Token 저장 실패");
        }

        // 7. 세션 고정 공격 방지를 위해 세션 ID 변경하기
        if(request.getSession(false) != null) {
            request.changeSessionId();
        }

        // 8. 인증된 회원정보를 SecurityContext에 저장하기
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        // 9. 인증 정보를 세션에 저장하기
        HttpSessionSecurityContextRepository repository =
                new HttpSessionSecurityContextRepository();

        repository.saveContext(context, request, response);

        // 10. Refresh Token을 HttpOnly Cookie에 저장하기
        ResponseCookie refreshTokenCookie = ResponseCookie.from(
                "refreshToken", jwtToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path(request.getContextPath() + "/auth")
                .maxAge(jwtTokenProvider.getRefreshTokenExpireTime())
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE,
                           refreshTokenCookie.toString());

        // 11. 생성된 JWT 토큰 반환하기
        return jwtToken;
    }
    
    
    // JWT 토큰 재발급하기
    @Override
    public JwtToken_kth reissue(String refreshToken,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        // 1. Refresh Token 존재 여부 확인하기
        if(refreshToken == null || refreshToken.isBlank()) {

            throw new BadCredentialsException("Refresh Token이 없습니다.");

        }

        // 2. Refresh Token 검증 및 회원 아이디 가져오기
        String userid;

        try {

            userid = jwtTokenProvider.getUseridFromRefreshToken(refreshToken);

        }
        catch(JwtException | IllegalArgumentException e) {

            throw new BadCredentialsException("유효하지 않은 Refresh Token입니다.");

        }

        // 3. 회원정보 조회하기
        MemberDTO_kth memberDto = dao.findByUsername(userid);

        if(memberDto == null) {

            throw new BadCredentialsException("존재하지 않는 회원입니다.");

        }

        long userseq = memberDto.getUserseq();

        // 기존 인증 회원과 Refresh Token 회원이 일치하는지 확인하기
        Authentication currentAuthentication =
                SecurityContextHolder.getContext().getAuthentication();

        if(currentAuthentication != null
                && currentAuthentication.isAuthenticated()
                && !(currentAuthentication instanceof
                     org.springframework.security.authentication.AnonymousAuthenticationToken)
                && !userid.equals(currentAuthentication.getName())) {

            throw new BadCredentialsException("인증 회원정보가 일치하지 않습니다.");

        }

        // 4. 기존 Refresh Token 해시값 조회하기
        String savedRefreshToken = dao.findRefreshToken(userseq);

        // 5. 전달받은 Refresh Token 해시 처리하기
        String hashedRefreshToken =
                jwtTokenProvider.hashRefreshToken(refreshToken);

        // 6. DB에 저장된 Refresh Token과 비교하기
        if(savedRefreshToken == null
                || !savedRefreshToken.equals(hashedRefreshToken)) {

            throw new BadCredentialsException("Refresh Token이 일치하지 않습니다.");

        }

        // 7. 회원의 인증 객체 생성하기
        memberDto.setPasswd(null);

        CustomUserDetails_kth principal =
                new CustomUserDetails_kth(memberDto);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        "",
                        principal.getAuthorities());

        // 8. 새로운 JWT 토큰 생성하기
        JwtToken_kth jwtToken =
                jwtTokenProvider.generateToken(authentication);

        // 9. 새로운 Refresh Token 해시 처리하기
        String newHashedRefreshToken =
                jwtTokenProvider.hashRefreshToken(jwtToken.getRefreshToken());

        // 10. DB에 저장된 Refresh Token 갱신하기
        Map<String, Object> paraMap = new HashMap<>();

        paraMap.put("userseq", userseq);
        paraMap.put("old_rt_value", hashedRefreshToken);
        paraMap.put("rt_value", newHashedRefreshToken);

        int n = dao.updateRefreshToken(paraMap);

        if(n != 1) {

            throw new BadCredentialsException("Refresh Token 갱신 실패");

        }

        // 11. 기존 세션 ID 변경하기
        if(request.getSession(false) != null) {

            request.changeSessionId();

        }

        // 12. 인증정보를 SecurityContext에 저장하기
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        // 13. 인증정보를 세션에 저장하기
        HttpSessionSecurityContextRepository repository =
                new HttpSessionSecurityContextRepository();

        repository.saveContext(context, request, response);

        // 14. 새로운 Refresh Token을 HttpOnly Cookie에 저장하기
        ResponseCookie refreshTokenCookie = ResponseCookie.from(
                "refreshToken", jwtToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path(request.getContextPath() + "/auth")
                .maxAge(jwtTokenProvider.getRefreshTokenExpireTime())
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE,
                           refreshTokenCookie.toString());

        // 15. 재발급된 JWT 토큰 반환하기
        return jwtToken;
    }
    
    // 로그아웃하기
    @Override
    public void logout(String refreshToken,
                       HttpServletRequest request,
                       HttpServletResponse response) {

        // 1. Refresh Token 존재 여부 확인하기
        if(refreshToken != null && !refreshToken.isBlank()) {

            String userid = null;

            try {

                // Refresh Token 검증 및 회원 아이디 가져오기
                userid = jwtTokenProvider.getUseridFromRefreshToken(refreshToken);

            }
            catch(JwtException | IllegalArgumentException e) {

                // 만료되거나 유효하지 않은 토큰이면 쿠키와 세션만 제거하기
            }

            if(userid != null) {

                // 2. 회원정보 조회하기
                MemberDTO_kth memberDto = dao.findByUsername(userid);

                if(memberDto != null) {

                    // 3. Refresh Token 해시 처리하기
                    String hashedRefreshToken =
                            jwtTokenProvider.hashRefreshToken(refreshToken);

                    // 4. DB에 저장된 Refresh Token 삭제하기
                    Map<String, Object> paraMap = new HashMap<>();

                    paraMap.put("userseq", memberDto.getUserseq());
                    paraMap.put("rt_value", hashedRefreshToken);

                    dao.deleteRefreshToken(paraMap);

                }

            }

        }

        // 5. Refresh Token 쿠키 삭제하기
        ResponseCookie refreshTokenCookie = ResponseCookie.from(
                "refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path(request.getContextPath() + "/auth")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE,
                           refreshTokenCookie.toString());

        // 6. 세션 및 인증정보 제거하기
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        SecurityContextLogoutHandler logoutHandler =
                new SecurityContextLogoutHandler();

        logoutHandler.logout(request, response, authentication);

    }
    
}