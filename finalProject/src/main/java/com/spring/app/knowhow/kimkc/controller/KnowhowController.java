package com.spring.app.knowhow.kimkc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/knowhow/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class KnowhowController {

	
	// 분류 화면 
	@GetMapping("list")
	public ModelAndView list(ModelAndView mav) {
		
		// referer
		
		
		
		mav.setViewName("knowhow/kimkc/knowhow");
		//  src/main/resources/templates/knowhow/kimkc/knowhow.html 페이지를 만들어야 한다.
		return mav;
	}
}
