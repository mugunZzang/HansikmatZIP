package com.spring.app.classify.kimkc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.classify.kimkc.domain.RecipeDTO;
import com.spring.app.classify.kimkc.service.ClassifyService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/classify/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class ClassfyController {

	
	private final ClassifyService service;
	
	// 분류 화면 
	@GetMapping("list")
	public ModelAndView list(ModelAndView mav,
							@RequestParam Map<String, String> map,
							HttpServletRequest request) {
		
		// referer
		String referer = request.getHeader("referer");
		if(referer == null) {
			mav.setViewName("redirect:/index");
			return mav;
		}
		
		
		
		int sizePerPage = 20;		// 한 페이지당 20개
		int totalPage = 0;			// 검색한 레시피 총 페이지수
		long totalDataCount = 0;	// 웹에 뿌려줄 검색 레시피 총 개수
		int currentShowPageNo = Integer.parseInt(map.get("currentShowPageNo")); // 현재 페이지 번호 
		
		String pageBar = "";
		
		
		
		// 검색 조건에 맞는 총 레시피 수 알아오기
		totalDataCount = service.getTotalDataCount(map);
		mav.addObject("totalDataCount",totalDataCount);
		
		// 총 페이지 수 구하기
		totalPage = (int) Math.ceil( (double)totalDataCount / sizePerPage );
		
		
		// 해당 페이지의 레시피 목록 갖고오기
		List<RecipeDTO> recipeList = service.getRecipeList(map);
		
		
		
		
		mav.setViewName("classify/kimkc/recipe_classify");
		//  src/main/resources/templates/classify/kimkc/recipe_classify.html 페이지를 만들어야 한다.
		return mav;
	}
	
	
	
	
	
	
	
}
