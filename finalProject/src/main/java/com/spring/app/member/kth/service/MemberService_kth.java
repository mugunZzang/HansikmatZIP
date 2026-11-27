package com.spring.app.member.kth.service;

import com.spring.app.member.kth.domain.MemberDTO_kth;
import com.spring.app.member.kth.security.JwtToken_kth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService_kth {

    // 아이디 중복검사
    int idDuplicateCheck(String userid);

    // 닉네임 중복검사
    int nicknameDuplicateCheck(String nickname);

    // 이메일 중복검사
    int emailDuplicateCheck(String email);
    
    // 이메일 인증번호 생성 및 발송
    String sendEmail(String email);
    
    // 회원가입 DB에 insert 하기
    int insertMember(MemberDTO_kth mbrdto) throws Exception;
    

    // JWT 로그인
    JwtToken_kth login(String userid, String passwd,
                       HttpServletRequest request,
                       HttpServletResponse response);
     // JWT 토큰 재발급하기
    JwtToken_kth reissue(String refreshToken,
                         HttpServletRequest request,
                         HttpServletResponse response);
    
    // 로그아웃하기
    void logout(String refreshToken,
                HttpServletRequest request,
                HttpServletResponse response);

}