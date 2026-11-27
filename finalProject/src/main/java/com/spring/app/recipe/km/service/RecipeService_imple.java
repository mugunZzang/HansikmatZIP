package com.spring.app.recipe.km.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.app.recipe.km.model.RecipeDAO;

import lombok.RequiredArgsConstructor;

//==== Service 선언 ==== //
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단

@Service
@RequiredArgsConstructor
public class RecipeService_imple implements RecipeService {

	private final RecipeDAO recipeDao;

	@Override
	@Transactional
	public int recipeRegister() {
		
				
//		int n1 = recipeDao.insertTable1();


//		int n2 = recipeDao.insertTable2();


//		int n3 = recipeDao.insertTable3();


//		int n4 = recipeDao.insertTable4();

		// INSERT가 실패한 경우(0행 입력) RuntimeException을 던져야 롤백됨
//		if (n1 == 0 || n2 == 0 || n3 == 0 || n4 == 0) {
//			throw new RuntimeException("레시피 등록 중 오류 발생");
//		}

		return 1;
	}

	
	
}
