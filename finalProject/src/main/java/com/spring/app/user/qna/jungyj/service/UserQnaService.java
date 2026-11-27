package com.spring.app.user.qna.jungyj.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.spring.app.qna.jungyj.domain.QnaDTO;

public interface UserQnaService {

	// 사용자 문의사항 등록
	void qnaRegister(QnaDTO qnaDto, List<MultipartFile> files) throws Exception;

	// 사용자 문의사항 조회(페이징 처리)
	List<QnaDTO> getPageQna(int userseq, int currentShowPageNo, int sizePerPage);

}
