package com.spring.app.member.kth.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails_kth implements UserDetails {

    private MemberDTO_kth memberDto;

    // 생성자
    public CustomUserDetails_kth(MemberDTO_kth memberDto) {
        this.memberDto = memberDto;
    }
    
    // 회원번호 반환하기
    public long getUserseq() {
        return memberDto.getUserseq();
    }

    // 회원의 아이디 반환하기
    @Override
    public String getUsername() {
        return memberDto.getUserid();
    }

    // 회원의 암호화된 비밀번호 반환하기
    @Override
    public String getPassword() {
        return memberDto.getPasswd();
    }

    // 회원의 권한 목록 반환하기
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for(String authority : memberDto.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }

        return authorities;
    }

    // 로그인한 회원의 닉네임 가져오기
    public String getNickname() {
        return memberDto.getNickname();
    }

}