package com.spring.app.classify.kimkc.model;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.spring.app.classify.kimkc.domain.RecipeDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ClassifyDAO_imple implements ClassifyDAO {

	@Qualifier("sqlsession")
	private final SqlSessionTemplate sqlsession;

	
	// 검색 조건에 맞는 총 레시피 수 알아오기
	@Override
	public long getTotalDataCount(Map<String, String> map) {
		long totalDataCount = sqlsession.selectOne("classify.getTotalDataCount", map);
		return totalDataCount;
	}


	// 해당 페이지의 레시피 목록 갖고오기
	@Override
	public List<RecipeDTO> getRecipeList(Map<String, String> map) {
		List<RecipeDTO> recipeList = sqlsession.selectList("classify.getRecipeList", map);
		return recipeList;
	}
	
}
