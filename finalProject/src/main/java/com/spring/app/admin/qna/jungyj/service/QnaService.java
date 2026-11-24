package com.spring.app.admin.qna.jungyj.service;

import java.util.List;

import com.spring.app.admin.qna.jungyj.domain.QnaDTO;
import com.sun.istack.Nullable;

public interface QnaService {

	// 전체 문의사항 조회
	List<QnaDTO> getList();

	// 답변 미완료(대기) 건수
	@Nullable
	long pendingCount();       

	// 답변 완료 건수
	@Nullable
	long completedCount();
}
