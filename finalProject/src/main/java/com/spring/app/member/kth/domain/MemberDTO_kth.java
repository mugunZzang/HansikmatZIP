package com.spring.app.member.kth.domain;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
	

	@Getter
	@Setter
	public class MemberDTO_kth {

	    private long userseq;              // 회원번호
	    private String social_userid;      // 소셜 아이디
	    private String userid;             // 로그인 아이디
	    private String passwd;             // 비밀번호
	    private String name;               // 이름
	    private String nickname;           // 닉네임
	    private String email;              // 이메일
	    private String mobile;             // 연락처
	    private int email_checked;         // 이메일 인증 여부
	    private String summary;            // 자기소개
	    private String profile_orgfilename; // 프로필 원본 파일명
	    private String profile_sysfilename; // 프로필 저장 파일명
	    
	    private List<String> authorities; // 회원의 권한 목록

	}

