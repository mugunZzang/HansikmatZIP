package com.spring.app.user.qna.jungyj.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.qna.jungyj.domain.QnaDTO;
import com.spring.app.user.qna.jungyj.service.UserQnaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/qna/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class UserQnaController {

	private final UserQnaService userQnaService;
	
	@GetMapping("register")
	public ModelAndView register(ModelAndView mav) {
		
		mav.setViewName("jungyj/qna/userqnaregister");
		//  src/main/resources/templates/admin/jungyj/qna/userqnaregister.html 페이지를 만들어야 한다.
		
		return mav;
	}
	
	@PostMapping("register")
	public ModelAndView register(ModelAndView mav,
			                     @RequestParam("files") List<MultipartFile> files,
			                     QnaDTO qnaDto) {
/*
		System.out.println("~~~ 확인용 qnaDto: " + qnaDto.toString());
		// ~~~ 확인용 qnaDto: QnaDTO(qnaseq=null, fk_userseq=null, qna_subject=테스트입니다., qna_content=문의사항등록테스트, qna_status=null, answer=null, answer_date=null, question_date=null, nickname=null)
		System.out.println("~~~ 확인용 첫 번째 파일: " + files.get(0).getOriginalFilename());
		System.out.println("~~~ 확인용 두 번째 파일: " + files.get(1).getOriginalFilename());
		~~~ 확인용 첫 번째 파일: 001.png
		~~~ 확인용 두 번째 파일: 002.png
*/		

		// 현재 로그인 기능은 구현되어 있지 않기에 회원 번호 강제로 삽입
		qnaDto.setFk_userseq(1);
		try {
			userQnaService.qnaRegister(qnaDto, files);
			mav.setViewName("jungyj/qna/userqnalist");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return mav;	
	}
}
