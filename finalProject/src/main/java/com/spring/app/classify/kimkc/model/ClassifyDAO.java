package com.spring.app.classify.kimkc.model;

import java.util.List;
import java.util.Map;

import com.spring.app.classify.kimkc.domain.RecipeDTO;

public interface ClassifyDAO {

	// 검색 조건에 맞는 총 레시피 수 알아오기
	long getTotalDataCount(Map<String, String> map);

	// 해당 페이지의 레시피 목록 갖고오기
	List<RecipeDTO> getRecipeList(Map<String, String> map);

}
