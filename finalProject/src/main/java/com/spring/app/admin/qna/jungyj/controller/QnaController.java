package com.spring.app.admin.qna.jungyj.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.admin.qna.jungyj.domain.QnaDTO;
import com.spring.app.admin.qna.jungyj.service.QnaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/admin/qna")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class QnaController {

	private final QnaService qnaService;
	
	@GetMapping("")
	public ModelAndView qna(ModelAndView mav) {
		
		// 실제 데이터 반환
		List<QnaDTO> qnaDtoList = qnaService.getList();
		
		mav.addObject("qnaDtoList", qnaDtoList);
		
		mav.addObject("pendingCount", qnaService.pendingCount());
		mav.addObject("completedCount", qnaService.completedCount());
		mav.setViewName("admin/jungyj/qna/qnaList");
		//  src/main/resources/templates/admin/jungyj/qna/qna.html 페이지를 만들어야 한다.
		
		return mav;
	}
}
