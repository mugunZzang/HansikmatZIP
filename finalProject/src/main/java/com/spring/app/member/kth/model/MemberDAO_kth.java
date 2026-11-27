package com.spring.app.member.kth.model;

import com.spring.app.member.kth.domain.MemberDTO_kth;
import java.util.Map;

public interface MemberDAO_kth {

	    // 아이디 중복검사
	    int idDuplicateCheck(String userid);

	    // 닉네임 중복검사
	    int nicknameDuplicateCheck(String nickname);

	    // 이메일 중복검사
	    int emailDuplicateCheck(String email);
	    
	    // 회원가입 DB에 insert 하기
		int insertMember(MemberDTO_kth mbrdto);

		//권한 부여하기
		int insertAuthority(long userseq);
		
		// 로그인 처리를 위해 사용되어지는 것임.
		MemberDTO_kth findByUsername(String userid);
		
		
		// Refresh Token 저장하기
		int saveRefreshToken(Map<String, Object> paraMap);

		// Refresh Token 조회하기
		String findRefreshToken(long userseq);

		// Refresh Token 갱신하기
		int updateRefreshToken(Map<String, Object> paraMap);

		// Refresh Token 삭제하기
		int deleteRefreshToken(Map<String, Object> paraMap);

	}

