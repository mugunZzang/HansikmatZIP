package com.spring.app.classify.kimkc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/classify/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class ClassfyController {

	// 분류 화면 
	@GetMapping("list")
	public ModelAndView list(ModelAndView mav) {
		
		// referer
		
		
		
		mav.setViewName("classify/kimkc/recipe_classify");
		//  src/main/resources/templates/classify/kimkc/recipe_classify.html 페이지를 만들어야 한다.
		return mav;
	}
	
	
	
	
	
	
	
}
