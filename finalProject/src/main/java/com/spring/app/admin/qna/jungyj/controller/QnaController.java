package com.spring.app.admin.qna.jungyj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/admin/qna")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class QnaController {

	@GetMapping("")
	public ModelAndView qna(ModelAndView mav) {
		mav.setViewName("admin/jungyj/qna/qnaList");
		//  src/main/resources/templates/admin/jungyj/qna/qna.html 페이지를 만들어야 한다.
		
		return mav;
	}
}
