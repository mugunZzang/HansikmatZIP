package com.spring.app.user.qna.jungyj.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.app.qna.jungyj.domain.QnaDTO;

public interface UserQnaService {

	void qnaRegister(QnaDTO qnaDto, List<MultipartFile> files) throws Exception;

}
