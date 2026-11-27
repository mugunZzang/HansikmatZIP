package com.spring.app.user.qna.jungyj.model;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;

import com.spring.app.qna.jungyj.domain.QnaDTO;
import com.spring.app.qna.jungyj.domain.QnaImagesDTO;

@Mapper
public interface UserQnaDAO {

	void insertQna(QnaDTO qnaDto);

	void insertQnaImage(QnaImagesDTO imgDto);
	
	List<String> findAllUsedQnaImageFilenames();

	// qna 총 개수
	int qnaTotalCount(int userseq);

	// 사용자 qna 목록 조회(페이징 처리)
	List<QnaDTO> qnaSelectAll(Map<String, Integer> paraMap);

}
