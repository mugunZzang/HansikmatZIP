package com.spring.app.admin.qna.jungyj.domain;

import java.time.LocalDateTime;

import com.spring.app.entity.jungyj.Qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                 // lombok 에서 사용하는 @Data 어노테이션은 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 합쳐놓은 종합선물세트인 것이다.
@AllArgsConstructor   // 모든 필드 값을 파라미터로 받는 생성자를 만들어주는 것
@NoArgsConstructor    // 파라미터가 없는 기본생성자를 만들어주는 것
@Builder              // 생성자 대신, 필요한 값만 선택해서 체이닝 방식으로 객체를 만들 수 있게 해주는 것.
public class QnaDTO {

	private Integer qnaseq;                        //   number                            not null     -- 문의 번호                
	private Integer fk_userseq;                    //   number                            not null     -- 회원 번호  
	private String qna_subject;                    //   NVARCHAR2(50)                     not null     -- 제목
	private String qna_content;                    //   NVARCHAR2(500)                    not null     -- 내용 
	private String qna_status;                     //   NVARCHAR2(10)   default '접수'     not null     -- 접수 상태 
	private String answer;                         //   NVARCHAR2(500)                                 -- 답변 내용 
	private LocalDateTime answer_date;             //   DATE                                           -- 답변 일시 
	private LocalDateTime question_date;           //   DATE            default sysdate   not null     -- 문의 일시 

    // 사용자 닉네임 필드(조회용)
    private String nickname;
    
	// Qna Entity를 통째로 받아서 DTO로 변환하는 생성자(목록 조회용)
	public QnaDTO(Qna qna) {
		this.qnaseq = qna.getQnaseq();
		this.fk_userseq = qna.getFkUserseq();
		this.qna_subject = qna.getQnaSubject();
		this.qna_content = qna.getQnaContent();
		this.qna_status = qna.getQnaStatus();
		this.answer = qna.getAnswer();
		this.answer_date = qna.getAnswerDate();
		this.question_date = qna.getQuestionDate();
		this.nickname = qna.getQnaUser().getNickname();
	}
	
	
}
