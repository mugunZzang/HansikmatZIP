package com.spring.app.event.kimkc.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.event.kimkc.domain.EventListDTO;
import com.spring.app.event.kimkc.service.EventListService;
import com.spring.app.kimkc.entity.Event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/event/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class EventListController {

	private final EventListService service;
	
	// 이벤트 리스트 불러오기 
	@GetMapping("list")
	public ModelAndView list(ModelAndView mav, HttpServletRequest request) {
		
		
		
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
						.dDay(ChronoUnit.DAYS.between(LocalDate.now(), entity.getEventEnddate()))
						.build() )
				.toList();
		
		
		
		
		mav.addObject("eventDtoList", eventDtoList);
		
		
		// 페이지바 만들기
		String ctxPath = request.getContextPath();
		String pageBar = makePageBar(currentShowPageNo, sizePerPage, totalPage, status, ctxPath);
		mav.addObject("ongoingPageBar", pageBar);
		
		
		
		
		mav.setViewName("event/kimkc/event_list");
		//  src/main/resources/templates/event/kimkc/evnet_list.html 페이지를 만들어야 한다.
		return mav;
	}
	
	
	// ===============================================================================================================================================================
	
	// 이벤트 탭 별로 리스트 가져오기(Ajax)
	@GetMapping("/tablist")
	public String tablist(Model model,
							@RequestParam(name="pageno") String currentShowPageNo,
							@RequestParam(name="status") String status,
							HttpServletRequest request) {
		
		int sizePerPage = 5;
		int pageno = Integer.parseInt(currentShowPageNo);
		int totalPage = 0;
		Page<Event> pageEvent = service.getPageEvent(pageno, sizePerPage, status);
		
		totalPage = pageEvent.getTotalPages(); // 전체 페이지 개수
		//System.out.println("전체 페이지 수: " + totalPage);
		
		if(totalPage > 0 && pageno > totalPage) {
			pageno = totalPage;
			pageEvent = service.getPageEvent(pageno, sizePerPage, status);
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
						.dDay(ChronoUnit.DAYS.between(LocalDate.now(), entity.getEventEnddate()))
						.build() )
				.toList();
		System.out.println("-------------------------------");
		System.out.println("누른 페이지 번호 : " + currentShowPageNo);
		System.out.println("선택중인 탭 : " + status);
		System.out.println("몇개 갖고옴? : " + eventDtoList.size());
		eventDtoList.forEach(dto -> System.out.println(dto.toString()));
		
		
		model.addAttribute("eventList", eventDtoList);
		
		String ctxPath = request.getContextPath();
		String pageBar = makePageBar(pageno, sizePerPage, totalPage, status, ctxPath);
		model.addAttribute("pageBar", pageBar);
		
		model.addAttribute("status", status);
		
		
		//mav.setViewName("event/kimkc/event_fragment :: eventListFragment");
		
		return "event/kimkc/event_fragment :: eventListFragment";
	}
	
	
	
	// ===============================================================================================================================================================
	
	// 페이지바 만드는 메소드
	private String makePageBar(int currentShowPageNo, int sizePerPage, int totalPage , String status, String ctxPath) {
		
		String pageBar = "";
		int blockSize = 10;
		int loop = 1;
		int pageno = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;

		pageBar = "<ul style='list-style:none;'>";

		// === [맨처음][이전] 만들기 === //	
		if (pageno != 1) {
		    pageBar += "<li style='display:inline-block; width:70px; font-size:12pt; cursor:pointer;' onclick=\"loadEventTab(1, '" + status + "')\">[맨처음]</li>";
		    pageBar += "<li style='display:inline-block; width:50px; font-size:12pt; cursor:pointer;' onclick=\"loadEventTab(" + (pageno - 1) + ", '" + status + "')\">[이전]</li>"; 
		}

		while (!(loop > blockSize || pageno > totalPage)) {
		    if (pageno == currentShowPageNo) {
		        pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; color:red; padding:2px 4px;'>" + pageno + "</li>";
		    } else {
		        pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; cursor:pointer;' onclick=\"loadEventTab(" + pageno + ", '" + status + "')\">" + pageno + "</li>"; 
		    }
		    loop++;
		    pageno++;
		}

		// === [다음][마지막] 만들기 === //
		if (pageno <= totalPage) {
		    pageBar += "<li style='display:inline-block; width:50px; font-size:12pt; cursor:pointer;' onclick=\"loadEventTab(" + pageno + ", '" + status + "')\">[다음]</li>";
		    pageBar += "<li style='display:inline-block; width:70px; font-size:12pt; cursor:pointer;' onclick=\"loadEventTab(" + totalPage + ", '" + status + "')\">[마지막]</li>"; 
		}

		pageBar += "</ul>";

		return pageBar;
	}
	
	
	
	// ===============================================================================================================================================================
	
	// 이벤트 상세 페이지
	@GetMapping("/detail/{seq}")
	public ModelAndView detail(ModelAndView mav,
							@PathVariable(name="seq") String seq) {
		
		
		
		mav.setViewName("event/kimkc/event_detail");
		
		return mav;
	}
	
	
	
}
