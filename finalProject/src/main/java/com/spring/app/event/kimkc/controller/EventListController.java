package com.spring.app.event.kimkc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/event/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class EventListController {

	// 분류 화면 
	@GetMapping("list")
	public ModelAndView list(ModelAndView mav) {
		
		// referer
		
		
		
		mav.setViewName("event/kimkc/event_list");
		//  src/main/resources/templates/event/kimkc/evnet_list.html 페이지를 만들어야 한다.
		return mav;
	}
}
