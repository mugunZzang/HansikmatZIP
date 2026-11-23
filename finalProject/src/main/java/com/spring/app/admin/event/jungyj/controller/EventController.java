package com.spring.app.admin.event.jungyj.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.admin.event.jungyj.domain.EventDTO;
import com.spring.app.admin.event.jungyj.domain.EventEntryDTO;
import com.spring.app.admin.event.jungyj.service.EventService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/admin/event")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class EventController {
	
	private final EventService eventService;

	//============================================= 이벤트 등록 ==================================================
	
	@GetMapping("/register")
	public ModelAndView register(ModelAndView mav) {
		mav.setViewName("admin/jungyj/event/eventRegister");
		//  src/main/resources/templates/admin/jungyj/event/eventRegister.html 페이지를 만들어야 한다.
		return mav;
	}
	
//	@PreAuthorize("isAuthenticated()")         // 관리자전용으로 설정해두어야 함
	@PostMapping("/register")
	public ModelAndView eventRegister(EventDTO eventDto,
									  ModelAndView mav,
			                          @RequestParam("eventImage") MultipartFile multipartFile    // Spring 에서 파일 받는 방법 RequestParam 은 HTML 에 있는 name 값
			                          ) {
/*	
		System.out.println(eventDto.getEvent_subject());
		System.out.println(eventDto.getEvent_contents());
		System.out.println(eventDto.getEvent_total_winners());
		System.out.println(eventDto.getEvent_startdate());
		System.out.println(eventDto.getEvent_enddate());
		System.out.println(multipartFile.getOriginalFilename());
*/
		
/*
		test_subject
		test_content
		2
		2026-11-20
		2026-11-22
		11.jpg
*/
		
		try {
			eventService.eventRegister(eventDto, multipartFile);
			mav.setViewName("redirect:/admin/event/list");
			//  src/main/resources/templates/admin/jungyj/event/eventList.html 페이지로 이동
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("admin/jungyj/error");
		    //  src/main/resources/templates/admin/jungyj/error.html 페이지로 이동
		}
		

		return mav;
	}
	
	
	
	//============================================= 이벤트 목록 조회 ===============================================
	
	@GetMapping("/list")
	public String list(@RequestParam(name = "status", defaultValue = "ongoing") String status,     // 탭 조건,
			           @RequestParam(value="pageno",    defaultValue="1") int currentShowPageNo, /* 현재 페이지번호 */   
			           HttpServletRequest request,
					   Model model) {
		
		// GET 방식인데 검색어가 있으므로 사용자가 URL 주소에 검색어를 직접 입력하여 장난칠 수 있으므로 
		// 이것을 방지하고자 Referer 를 사용하도록 한다.
/*
		String referer = request.getHeader("Referer");
		if(referer == null) {
			return "redirect:/index";
		}
*/
		
		// 페이징 처리 시작
		int sizePerPage = 5;     // 한 페이지당 보여질 행의 개수
		int totalPage = 0;       // 전체 페이지 개수
		long totalDataCount = 0; // 전체 데이터의 개수
		String pageBar = "";     // 페이지바
		
		if(currentShowPageNo < 1) {
			currentShowPageNo = 1;
		}
		
		Page<EventDTO> pageEvent = eventService.getPageEvent(status, currentShowPageNo, sizePerPage);
		
		totalPage = pageEvent.getTotalPages();    // 전체 페이지 개수
		
		if(totalPage > 0 && currentShowPageNo > totalPage) {
			currentShowPageNo = totalPage;
			pageEvent = eventService.getPageEvent(status, currentShowPageNo, sizePerPage);
		}
		
		totalDataCount = pageEvent.getTotalElements(); // 전체 데이터의 개수
		
		// 현재 페이지의 데이터 목록인 List<Event> 를 List<EventDTO> 로 변환
		List<EventDTO> eventList = pageEvent.getContent();

	    model.addAttribute("eventList", eventList);
	 // === 페이지바 만들기 시작 === //
	    int blockSize = 10;
	    int loop = 1;
	    int pageno = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;

	    pageBar = "<ul class='pagination justify-content-center'>";
	    String url = request.getRequestURI();

	    // === [맨처음][이전] 만들기 === //
	    if (pageno != 1) {
	        pageBar += "<li class='page-item'><a class='page-link' href='" + url + "?status=" + status + "&pageno=1'>&laquo;</a></li>";
	        pageBar += "<li class='page-item'><a class='page-link' href='" + url + "?status=" + status + "&pageno=" + (pageno - 1) + "'>&lsaquo;</a></li>";
	    }

	    while (!(loop > blockSize || pageno > totalPage)) {
	        if (pageno == currentShowPageNo) {
	            pageBar += "<li class='page-item active'><span class='page-link'>" + pageno + "</span></li>";
	        } else {
	            pageBar += "<li class='page-item'><a class='page-link' href='" + url + "?status=" + status + "&pageno=" + pageno + "'>" + pageno + "</a></li>";
	        }
	        loop++;
	        pageno++;
	    }

	    // === [다음][마지막] 만들기 === //
	    if (pageno <= totalPage) {
	        pageBar += "<li class='page-item'><a class='page-link' href='" + url + "?status=" + status + "&pageno=" + pageno + "'>&rsaquo;</a></li>";
	        pageBar += "<li class='page-item'><a class='page-link' href='" + url + "?status=" + status + "&pageno=" + totalPage + "'>&raquo;</a></li>";
	    }

	    pageBar += "</ul>";
	    
	    
	    model.addAttribute("pageBar", pageBar);
	    model.addAttribute("totalDataCount", totalDataCount);
	    model.addAttribute("currentShowPageNo", currentShowPageNo);
	    model.addAttribute("sizePerPage", sizePerPage);
	    // === 페이지바 만들기 끝 === //
	    
		
		model.addAttribute("status", status);
		model.addAttribute("ongoingCount", eventService.getOngoingCount());
		model.addAttribute("endedCount", eventService.getEndedCount());
		
		return "admin/jungyj/event/eventList";
		//  src/main/resources/templates/admin/jungyj/event/eventList.html 페이지를 만들어야 한다.
	}
	
	
	//============================================== 이벤트 상세 페이지 =============================================
	
	@GetMapping("/detail")
	public ModelAndView detail(ModelAndView mav,
			                   @RequestParam(name = "eventseq") int eventseq) {
		
//		System.out.println("~~~ 확인용 eventseq: " + eventseq);
//		~~~ 확인용 eventseq: 7
		
		EventDTO eventDto = eventService.detail(eventseq);
		List<EventEntryDTO> allEntries = eventService.getEntryList(eventseq);
		
		// 참가한 레시피 목록
		List<EventEntryDTO> entryList = allEntries.stream()
									              .filter(entry -> entry.getChk_win() == 0)
									              .toList();
		// 당첨자 레시피 목록
	    List<EventEntryDTO> winnerList = allEntries.stream()
	                                               .filter(entry -> entry.getChk_win() == 1)
	                                               .toList();
		
	    boolean isDrawingPassed = LocalDate.now().isAfter(eventDto.getEvent_drawingdate());
	    
		mav.addObject("eventDto", eventDto);
		mav.addObject("entryList", entryList);   // 중단 카드: 참가한 레시피
	    mav.addObject("winnerList", winnerList); // 하단 카드: 당첨 레시피
	    mav.addObject("isDrawingPassed", isDrawingPassed); // 발표일이 지났는지 여부
	    
		mav.setViewName("admin/jungyj/event/eventDetail");
		//  src/main/resources/templates/admin/jungyj/event/eventDetail.html 페이지를 만들어야 한다.
		return mav;
	}
	
	@PostMapping("/win")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> winEntries(@RequestParam("eventseq") Integer eventseq,
	                                                      @RequestParam("entryseqs") List<Integer> entryseqs) {
		Map<String, Object> result = new HashMap<>();
	    try {
	        eventService.winEntries(eventseq, entryseqs);
	        result.put("success", true);
	        result.put("message", "당첨 처리가 완료되었습니다.");
	        return ResponseEntity.ok(result);
	    } catch (IllegalArgumentException e) {
	        result.put("success", false);
	        result.put("message", e.getMessage());
	        return ResponseEntity.badRequest().body(result);
	    } catch (Exception e) {
	        e.printStackTrace();
	        result.put("success", false);
	        result.put("message", "당첨 처리 중 오류가 발생했습니다.");
	        return ResponseEntity.internalServerError().body(result);
	    }
	}
	
	//================================================ 이벤트 수정 =================================================
	
	@GetMapping("/edit")
	public ModelAndView edit(ModelAndView mav,
			                 @RequestParam(name = "eventseq") int eventseq) {
		
//		System.out.println("~~~ 확인용 eventseq: " + eventseq);
//		~~~ 확인용 eventseq: 7
		
		// 기존 정보 전달
		EventDTO eventDto = eventService.detail(eventseq);
		
		mav.addObject("eventDto", eventDto);
		
		mav.setViewName("admin/jungyj/event/eventEdit");
		//  src/main/resources/templates/admin/jungyj/event/eventEdit.html 페이지를 만들어야 한다.
		return mav;
	}
	
	@PostMapping("/edit")
	public ModelAndView eventEdit(ModelAndView mav,
							      EventDTO eventDto,
			                      @RequestParam("eventImage") MultipartFile multipartFile    // Spring 에서 파일 받는 방법 RequestParam 은 HTML 에 있는 name 값
			                ) {
/*
		System.out.println(eventDto.getEvent_subject());
		System.out.println(eventDto.getEvent_contents());
		System.out.println(eventDto.getEvent_total_winners());
		System.out.println(eventDto.getEvent_startdate());
		System.out.println(eventDto.getEvent_enddate());
		System.out.println(multipartFile.getOriginalFilename());
		
		진행중인 이벤트 수정 첫 번쨰 테스트 제목
		진행중인 이벤트 수정 첫번쨰 테스트 내용
		11
		2026-11-20
		2026-11-26
		스크린샷 2026-03-05 122808.png
*/
		
		try {
			eventService.eventEdit(eventDto, multipartFile);
			mav.setViewName("redirect:/admin/event/detail?eventseq=" + eventDto.getEventseq());
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("admin/jungyj/error");
		}
		
		return mav;
	}
	
	

}
