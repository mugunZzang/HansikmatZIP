package com.spring.app.admin.qna.jungyj.service;


import static com.spring.app.entity.jungyj.QQna.qna;
import static com.spring.app.entity.jungyj.QAppUser.appUser;

import java.util.List;

import org.springframework.stereotype.Service;

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
	// 전체 문의사항 조회
	@Override
	public List<QnaDTO> getList() {
		
		List<Qna> qnaEntityList = jPAQueryFactory
										        .selectFrom(qna)
										        .join(qna.qnaUser, appUser).fetchJoin()
										        .orderBy(qna.qnaseq.desc())
										        .fetch();
		
		List<QnaDTO> qnaDtoList = qnaEntityList.stream()
				                               .map(QnaDTO::new)
				                               .toList();
		
		return qnaDtoList;
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

}
