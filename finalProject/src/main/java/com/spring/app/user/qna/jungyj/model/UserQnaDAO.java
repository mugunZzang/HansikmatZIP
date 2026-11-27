package com.spring.app.user.qna.jungyj.model;

import org.apache.ibatis.annotations.Mapper;

import com.spring.app.qna.jungyj.domain.QnaDTO;
import com.spring.app.qna.jungyj.domain.QnaImagesDTO;

@Mapper
public interface UserQnaDAO {

	void insertQna(QnaDTO qnaDto);

	void insertQnaImage(QnaImagesDTO imgDto);

}
