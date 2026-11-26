package com.spring.app.security.yukmg.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/* ===== (#스프링시큐리티) ===== */
// == Lombok 라이브러리에서 제공하는 어노테이션 == 
// @Getter                   // private 으로 설정된 필드 변수를 외부에서 접근하여 사용하도록 getter()메소드를 만들어 주는 것.
// @Setter                   // private 으로 설정된 필드 변수를 외부에서 접근하여 수정하도록 setter()메소드를 만들어 주는 것.
// @ToString                 // 객체를 문자열로 표현할 때 사용
// @RequiredArgsConstructor  // final 필드 또는 @NonNull이 붙은 필드에 대해 이 필드만 포함하는 생성자를 자동으로 생성해준다.
// @AllArgsConstructor       // 모든 필드 값을 파라미터로 받는 생성자를 만들어주는 것
// @NoArgsConstructor        // 파라미터가 없는 기본생성자를 만들어주는 것
@Data                        // lombok 에서 사용하는 @Data 어노테이션은 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 합쳐놓은 종합선물세트인 것이다.
public class UserDTO {
 /* 먼저 오라클에서
    회원 테이블(인증 테이블)인 tbl_member_security 테이블과
    어쏘러티(권한, 역할) 테이블인 tbl_authorities 테이블을 만들어야 한다. */
	
	private Long userseq;
	private String social_userid;
    private String userid;
    private String passwd;
    private String name;
    private String nickname;
    private String email;
    private String hp1;                // DB 컬럼명이 아님 
	private String hp2;                // DB 컬럼명이 아님
	private String hp3; 			   // DB 컬럼명이 아님 
    private String mobile;			   // 연락처 (AES-256 암호화/복호화 대상)
    private int email_checked;		   // 이메일 인증 여부(1: 인증완료, 0:미인증)    	
    private String summary;					// 자기소개
    private String profile_orgfilename;		// 프로필 원본 파일명	
    private String profile_sysfilename;		// 프로필 저장 파일명
    private int recipe_count;	// 레시피 수
    private int total_scrap_count;	// 스크랩 수
    
    private List<String> authorities;  // 권한
	
}
