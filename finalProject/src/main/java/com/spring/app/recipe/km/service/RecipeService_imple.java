package com.spring.app.recipe.km.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.spring.app.recipe.km.model.RecipeDAO;

import lombok.RequiredArgsConstructor;

//==== Service 선언 ==== //
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단

@Service
@RequiredArgsConstructor
public class RecipeService_imple implements RecipeService {

	private final RecipeDAO recipeDao;

	
	
}
