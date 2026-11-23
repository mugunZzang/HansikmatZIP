package com.spring.app.classify.kimkc.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.spring.app.classify.kimkc.domain.RecipeDTO;
import com.spring.app.classify.kimkc.model.ClassifyDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassifyService_imple implements ClassifyService {

	private final ClassifyDAO dao;

	
	// 검색 조건에 맞는 총 레시피 수 알아오기
	@Override
	public long getTotalDataCount(Map<String, String> map) {
		
	 	long totalDataCount = dao.getTotalDataCount(map);
		
		return totalDataCount;
	}

	
	
	// 해당 페이지의 레시피 목록 갖고오기
	@Override
	public List<RecipeDTO> getRecipeList(Map<String, String> map) {
		List<RecipeDTO> recipeList = dao.getRecipeList(map);
		return recipeList;
	}
	
	
	
	
	
}
