package com.spring.app.event.kimkc.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.event.kimkc.domain.EventListDTO;
import com.spring.app.event.kimkc.service.EventListService;
import com.spring.app.kimkc.entity.Event;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/event/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class EventListController {

	private final EventListService service;
	
	// 분류 화면 
	@GetMapping("list")
	public ModelAndView list(ModelAndView mav) {
		
		
		
		// referer
		
		
		// 기본 페이지인 [진행중인 이벤트] 탭의 1페이지 이벤트 리스트 갖고와야 함
		String status = "ongoing";
		int currentShowPageNo = 1;
		int sizePerPage = 5;
		int totalPage = 0;
		Page<Event> pageEvent = service.getPageEvent(currentShowPageNo, sizePerPage, status);

		
		totalPage = pageEvent.getTotalPages(); // 전체 페이지 개수
		//System.out.println("전체 페이지 수: " + totalPage);
		
		if(totalPage > 0 && currentShowPageNo > totalPage) {
			currentShowPageNo = totalPage;
			pageEvent = service.getPageEvent(currentShowPageNo, sizePerPage, status);
		}
		
		
		List<Event> eventList = pageEvent.getContent();
		
		// 엔티티를 DTO로 변환
		List<EventListDTO> eventDtoList = eventList.stream()
				.map(entity -> EventListDTO.builder()
						.eventseq(entity.getEventSeq())
						.event_subject(entity.getEventSubject())
						.event_startdate(entity.getEventStartdate())
						.event_enddate(entity.getEventEnddate())
						.event_image_filename(entity.getEventImageFilename())
						.build() )
				.toList();
		
		
		
		
		mav.addObject("eventDtoList", eventDtoList);
		
		
		
		mav.setViewName("event/kimkc/event_list");
		//  src/main/resources/templates/event/kimkc/evnet_list.html 페이지를 만들어야 한다.
		return mav;
	}
	
	
	// ===============================================================================================================================================================
	
	// ajax 랑 놀기
	
	
	// ===============================================================================================================================================================
	
	
	private String makePageBar(int currentShowPageNo, int sizePerPage, String status) {
		
		
		return "";
	}
	
	
}
