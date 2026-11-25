package com.spring.app.admin.qna.jungyj.service;


import static com.spring.app.entity.jungyj.QAppUser.appUser;
import static com.spring.app.entity.jungyj.QQna.qna;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.app.admin.qna.jungyj.domain.QnaDTO;
import com.spring.app.admin.qna.jungyj.repository.QnaRepository;
import com.spring.app.entity.jungyj.Qna;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaService_imple implements QnaService {
	
	private final QnaRepository qnaRepository;
	
	
	private final JPAQueryFactory jPAQueryFactory;

	
	// 전체 문의사항 조회(페이징처리)
	@Override
	public Page<QnaDTO> getPageQna(String status, int currentShowPageNo, int sizePerPage) {
		Page<QnaDTO> page = Page.empty();      // 기본값으로 내용이 없는 빈 페이지. null이 아니기에 안전함
		try {
			Pageable pageable = PageRequest.of(currentShowPageNo-1, sizePerPage, Sort.by(Sort.Direction.DESC, "eventseq"));

			BooleanExpression condition = Expressions.TRUE; 
			
			// 접수 / 답변완료 

			if ("pending".equals(status)) {
				condition = condition.and(qna.qnaStatus.eq("접수"));
	        } else {
	            condition = condition.and(qna.qnaStatus.eq("답변완료"));
	        }
			
			List<Qna> qnaEntityList = jPAQueryFactory
											        .selectFrom(qna)
											        .join(qna.qnaUser, appUser).fetchJoin()
											        .where(condition) 
											        .offset(pageable.getOffset())            // 페이지 시작 위치 (예: 0, 10, 20...).  pageable.getOffset() 은 Spring Data JPA에서 페이징 처리 시 데이터베이스에서 조회를 시작할 위치를 말하는 것이다.
													.limit(pageable.getPageSize())           // 한 페이지당 데이터 수
											        .orderBy(qna.qnaseq.desc())
											        .fetch();
						
			
			// 특정 조건에 맞는 게시글의 총 개수를 조회
	        Long total = jPAQueryFactory
			                .select(qna.count())
			                .from(qna)
			                .where(condition)
			                .fetchOne(); // 단일 결과를 반환한다. count 는 하나의 숫자만 반환되므로 fetchOne()을 사용한다.
				
	        List<QnaDTO> eventdtoList = qnaEntityList.stream()
	                .map(QnaDTO::new)
	                .toList();
	        

			long totalCount = (total != null) ? total : 0L;
	        page = new PageImpl<>(eventdtoList, pageable, totalCount);
	        
			
		} catch(Exception e) {
        	e.printStackTrace();
        }
		return page;
	}
	
	
	// 답변 미완료(대기) 건수
	@Override
	public long pendingCount() {
		return qnaRepository.countByQnaStatus("접수");
	}
	
	// 답변 완료 건수
	@Override
	public long completedCount() {
		return qnaRepository.countByQnaStatus("답변완료");
	}

	// 답변 등록(UPDATE)
	// JPA의 영속상태를 이용한 Dirty Checking
	@Override
	@Transactional
	public int answerRegister(Map<String, String> paraMap) {
		String answer = paraMap.get("answer");
		int qnaSeq = Integer.parseInt(paraMap.get("qnaSeq"));
		
		Qna qnaEntity = getQnaEntity(qnaSeq);  // 영속성 객체 조회

		LocalDateTime now = LocalDateTime.now();
		
		qnaEntity.setAnswer(answer);         // 객체 필드 변화
		qnaEntity.setQnaStatus("답변완료");     // 객체 필드 변화
		qnaEntity.setAnswerDate(now);        // 객체 필드 변화

		
		return 1;
	}


	// Qna Entity 1건 조회
	private Qna getQnaEntity(int qnaSeq) {
	    return qnaRepository.findById(qnaSeq)
	            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 QnA입니다. eventseq=" + qnaSeq));
	} // private Qna getQnaEntity(int qnaSeq)--------------------------


}
