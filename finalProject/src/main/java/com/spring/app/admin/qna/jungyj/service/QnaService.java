package com.spring.app.admin.qna.jungyj.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.spring.app.admin.qna.jungyj.domain.QnaDTO;

public interface QnaService {
	
	// 전체 문의사항 조회(페이징처리)
	Page<QnaDTO> getPageQna(String status, int currentShowPageNo, int sizePerPage);

	// 답변 미완료(대기) 건수
	long pendingCount();       

	// 답변 완료 건수
	long completedCount();

	// 답변 등록
	int answerRegister(Map<String, String> paraMap);
}
