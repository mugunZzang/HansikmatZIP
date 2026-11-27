package com.spring.app.member.kth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.app.member.kth.domain.CustomUserDetails_kth;
import com.spring.app.member.kth.domain.MemberDTO_kth;
import com.spring.app.member.kth.model.MemberDAO_kth;

@Service
public class CustomUserDetailsService_kth implements UserDetailsService {

    @Autowired
    private MemberDAO_kth dao;

    // 로그인 인증에 필요한 회원정보 조회하기
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        MemberDTO_kth memberDto = dao.findByUsername(username);

        // 조회된 회원정보가 없는 경우
        if(memberDto == null) {
            throw new UsernameNotFoundException("존재하지 않는 회원입니다.");
        }

        // 조회된 회원정보를 Spring Security에 전달하기
        return new CustomUserDetails_kth(memberDto);
    }

}