package com.spring.app.classify.kimkc.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDTO {

	private Long recipeseq;             // 레시피 번호 (PK)
    private Long fk_userseq;            // 회원 번호 (FK)
    private String r_subject;           // 제목
    private String r_orgimgname;        // 대표 사진 원본 파일명
    private String r_imgname;           // 대표 사진 저장 파일명
    private String r_intro;             // 레시피 소개
    private String r_url;               // 동영상 주소
    private String r_time;              // 조리 시간
    private String r_level;             // 난이도
    private String people;              // 인원수
    private String paid_check;          // 유료 여부
    private String tip_caution;         // 팁 및 주의사항
    private Integer r_views;            // 조회수
    private String ai_review;           // AI 리뷰요약
    private Integer ai_totalcalorie;    // AI 총 칼로리 계산
    private LocalDateTime r_writedate;  // 작성일시
    private String situation_ca;        // 상황 카테고리명
    private String type_ca;             // 종류 카테고리명
    private String main_ca;             // 주재료 카테고리명
    private String recipe_ca;           // 조리방법 카테고리명
    private String view_check;          // 게시 상태
	
    
    // tbl_member 컬럼
    private String nickname; 			// 닉네임(join)
    private String profile_sysfilename;	// 프로필 시스템파일명(join)
    
    
    // 별도 컬럼
    private Double avg_rating;			// 레시피 평점
}
