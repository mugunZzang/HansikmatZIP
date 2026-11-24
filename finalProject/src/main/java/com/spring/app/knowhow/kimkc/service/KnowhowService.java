package com.spring.app.knowhow.kimkc.service;

import org.springframework.data.domain.Page;

import com.spring.app.kimkc.entity.Recipe;

public interface KnowhowService {

	// 현재 페이지에 해당하는 노하우 리스트 불러오기
	Page<Recipe> getPageKnowhow(int currentShowPageNo_int, int sizePerPage);

}
