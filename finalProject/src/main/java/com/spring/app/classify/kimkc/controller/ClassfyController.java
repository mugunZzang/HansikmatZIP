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
		/*
		 * String referer = request.getHeader("referer"); if(referer == null) {
		 * mav.setViewName("redirect:/index"); return mav; }
		 */
		/*
		System.out.println("종류별:" + map.get("cf_type"));
		System.out.println("주재료별:" + map.get("cf_main"));
		System.out.println("상황별:" + map.get("cf_situation"));
		System.out.println("방법별:" + map.get("cf_recipe"));
		System.out.println("조리시간:" + map.get("cf_time"));
		System.out.println("난이도:" + map.get("cf_level"));
		System.out.println("인원수:" + map.get("cf_people"));
		System.out.println("멤버십:" + map.get("cf_membership"));
		System.out.println("정렬기준:" + map.get("cf_sort"));
		*/
		int sizePerPage = 20;		// 한 페이지당 20개
		int totalPage = 0;			// 검색한 레시피 총 페이지수
		long totalDataCount = 0;	// 웹에 뿌려줄 검색 레시피 총 개수
		String currentShowPageNo = "1";  // 현재 페이지 번호 
		
		if(map.get("currentShowPageNo")!=null) {
			currentShowPageNo = map.get("currentShowPageNo");
		}
		map.put("currentShowPageNo", currentShowPageNo);
		
		String pageBar = "";
		
		
		
		// 검색 조건에 맞는 총 레시피 수 알아오기
		totalDataCount = service.getTotalDataCount(map);
		System.out.println("토탈:"+totalDataCount);
		mav.addObject("totalDataCount",totalDataCount);
		
		// 총 페이지 수 구하기
		totalPage = (int) Math.ceil( (double)totalDataCount / sizePerPage );
		
		
		// 해당 페이지의 레시피 목록 갖고오기
		List<RecipeDTO> recipeList = service.getRecipeList(map);
		for(RecipeDTO dto : recipeList) {
			System.out.println("레시피번호:" + dto.getRecipeseq());
			System.out.println("레시피명:" +dto.getR_subject());
			System.out.println("작성자:" + dto.getNickname());
		}
		mav.addObject("recipeList", recipeList);
		
		mav.addObject("resultMap", map);
		
		mav.setViewName("classify/kimkc/recipe_classify");
		//  src/main/resources/templates/classify/kimkc/recipe_classify.html 페이지를 만들어야 한다.
		return mav;
	}
	
	
	
	
	
	
	
}
