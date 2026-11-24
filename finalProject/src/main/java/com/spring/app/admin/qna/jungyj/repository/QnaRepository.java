package com.spring.app.admin.qna.jungyj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.app.entity.jungyj.Qna;

public interface QnaRepository extends JpaRepository<Qna, Integer>{
	
	long countByQnaStatus(String qnaStatus);
	
}
