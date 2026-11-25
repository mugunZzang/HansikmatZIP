package com.spring.app.admin.qna.jungyj.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.app.admin.qna.jungyj.domain.QnaDTO;
import com.spring.app.admin.qna.jungyj.service.QnaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/admin/qna")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class QnaController {

	private final QnaService qnaService;
	
	@GetMapping("")
	public String qna(@RequestParam(name = "status", defaultValue = "pending") String status,     // 탭 조건,
			          @RequestParam(value="pageno",    defaultValue="1") int currentShowPageNo, /* 현재 페이지번호 */   
			          HttpServletRequest request,
			          Model model) {
		
		
		// 페이징 처리 시작
		int sizePerPage = 10;     // 한 페이지당 보여질 행의 개수
		int totalPage = 0;       // 전체 페이지 개수
		long totalDataCount = 0; // 전체 데이터의 개수
		String pageBar = "";     // 페이지바
		
		if(currentShowPageNo < 1) {
			currentShowPageNo = 1;
		}
		
		Page<QnaDTO> pageQna = qnaService.getPageQna(status, currentShowPageNo, sizePerPage);
		
		totalPage = pageQna.getTotalPages();    // 전체 페이지 개수
		
		if(totalPage > 0 && currentShowPageNo > totalPage) {
			currentShowPageNo = totalPage;
			pageQna = qnaService.getPageQna(status, currentShowPageNo, sizePerPage);
		}
		
		totalDataCount = pageQna.getTotalElements(); // 전체 데이터의 개수
		
		// Service에서는 Entity를 이미 DTO로 반환하여 줌
		List<QnaDTO> qnaList = pageQna.getContent();

	    model.addAttribute("qnaDtoList", qnaList);
	 // === 페이지바 만들기 시작 === //
	    int blockSize = 10;
	    int loop = 1;
	    int pageno = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;

	    pageBar = "<ul class='pagination justify-content-center'>";
	    
	    String contextPath = request.getContextPath();
	    String url = contextPath+"/admin/qna";
	    

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
		model.addAttribute("pendingCount", qnaService.pendingCount());
		model.addAttribute("completedCount", qnaService.completedCount());

		
		
		return "admin/jungyj/qna/qnaList";
		//  src/main/resources/templates/admin/jungyj/qna/qna.html 페이지를 만들어야 한다.
	}
	
	// 답변 등록
	@PostMapping("answerRegister")
	@ResponseBody
	public Map<String, Integer> answerRegister(@RequestParam Map<String, String> paraMap){
		int result = 0;
		
		try {
	        result = qnaService.answerRegister(paraMap);
	    } catch (Exception e) {
	        e.printStackTrace();
	        result = 0;
	    }
		
		Map<String, Integer> map = new HashMap<>();
	    map.put("answer", result);
		
		return map;
	}
}
