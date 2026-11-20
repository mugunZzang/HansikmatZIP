package com.spring.app.admin.event.jungyj.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.common.FileManager;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/admin/event")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class EventController {
	
	// === 파일업로드 및 파일다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection) === 
	private final FileManager fileManager;
	
	@Value("${file.eventimageimage-dir}")
	private String eventimageimageDir; // 글쓰기시 스마트에디터를 통해 사진을 올리는 경로를 잡아주는 것이다.

	//============================================= 이벤트 등록 ==================================================
	
	@GetMapping("/register")
	public ModelAndView register(ModelAndView mav) {
		mav.setViewName("admin/jungyj/event/eventRegister");
		//  src/main/resources/templates/admin/jungyj/event/eventRegister.html 페이지를 만들어야 한다.
		return mav;
	}
	
//	@PreAuthorize("isAuthenticated()")         // 관리자전용으로 설정해두어야 함
	@PostMapping("/register")
	public ModelAndView eventRegister(ModelAndView mav,
			                          @RequestParam("eventImage") MultipartFile multipartFile    // Spring 에서 파일 받는 방법 RequestParam 은 HTML 에 있는 name 값
			                          ) {
		
		return mav;
	}
	
	
	
	//============================================= 이벤트 목록 조회 ===============================================
	
	@GetMapping("/list")
	public ModelAndView list(ModelAndView mav) {
		mav.setViewName("admin/jungyj/event/eventList");
		//  src/main/resources/templates/admin/jungyj/event/eventList.html 페이지를 만들어야 한다.
		return mav;
	}
	
	
	//============================================== 이벤트 상세 페이지 =============================================
	
	@GetMapping("/detail")
	public ModelAndView detail(ModelAndView mav) {
		mav.setViewName("admin/jungyj/event/eventDetail");
		//  src/main/resources/templates/admin/jungyj/event/eventDetail.html 페이지를 만들어야 한다.
		return mav;
	}
	
	
	//================================================ 이벤트 수정 =================================================
	
	@GetMapping("/edit")
	public ModelAndView edit(ModelAndView mav) {
		mav.setViewName("admin/jungyj/event/eventEdit");
		//  src/main/resources/templates/admin/jungyj/event/eventEdit.html 페이지를 만들어야 한다.
		return mav;
	}
	
	

}
