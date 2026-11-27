

package com.spring.app.member.kth.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.app.common.AES256;
import com.spring.app.member.kth.domain.MemberDTO_kth;
import java.util.Map;

@Repository
public class MemberDAO_imple_kth implements MemberDAO_kth {

	@Autowired
	private AES256 aes256;
	
    @Autowired
    private SqlSession sqlsession;

    // 아이디 중복검사
    @Override
    public int idDuplicateCheck(String userid) {
        int n = sqlsession.selectOne("member_kth.idDuplicateCheck", userid);
        return n;
    }

    // 닉네임 중복검사
    @Override
    public int nicknameDuplicateCheck(String nickname) {
        int n = sqlsession.selectOne("member_kth.nicknameDuplicateCheck", nickname);
        return n;
    }

    // 이메일 중복검사
    @Override
    public int emailDuplicateCheck(String email) {
        int n = sqlsession.selectOne("member_kth.emailDuplicateCheck", email);
        return n;
    }

    // 회원가입 DB에 insert 하기
	@Override
	public int insertMember(MemberDTO_kth mbrdto) {
		  int n = sqlsession.insert("member_kth.memberRegister", mbrdto);

		    return n;
		
	}
    
	//권한 부여하기
	@Override
	public int insertAuthority(long userseq) {
		 int n =sqlsession.insert("member_kth.insertAuthority", userseq);
			return n;
	}
	
	// 로그인 처리를 위해 사용되어지는 것임.
	@Override
	public MemberDTO_kth findByUsername(String userid) {

	    MemberDTO_kth memberDto =
	        sqlsession.selectOne("member_kth.findByUsername", userid);

	    if(memberDto != null) {

	        try {
	            // 이메일 복호화
	            memberDto.setEmail(aes256.decrypt(memberDto.getEmail()));

	            // 휴대폰번호 복호화
	            memberDto.setMobile(aes256.decrypt(memberDto.getMobile()));

	        } catch(UnsupportedEncodingException | GeneralSecurityException e) {
	            e.printStackTrace();
	        }

	        // 회원의 권한 목록 조회하기
	        List<String> authorityList =
	            sqlsession.selectList("member_kth.authorityList", memberDto.getUserseq());

	        memberDto.setAuthorities(authorityList);
	    }

	    return memberDto;
	}

	// Refresh Token 저장하기
	@Override
	public int saveRefreshToken(Map<String, Object> paraMap) {

	    int n = sqlsession.update("member_kth.saveRefreshToken", paraMap);

	    return n;
	}

	// Refresh Token 조회하기
	@Override
	public String findRefreshToken(long userseq) {

	    String refreshToken =
	        sqlsession.selectOne("member_kth.findRefreshToken", userseq);

	    return refreshToken;
	}

	// Refresh Token 갱신하기
	@Override
	public int updateRefreshToken(Map<String, Object> paraMap) {

	    int n = sqlsession.update("member_kth.updateRefreshToken", paraMap);

	    return n;
	}

	// Refresh Token 삭제하기
	@Override
	public int deleteRefreshToken(Map<String, Object> paraMap) {

	    int n = sqlsession.delete("member_kth.deleteRefreshToken", paraMap);

	    return n;
	}
	
	
	
	
	
	
	
}