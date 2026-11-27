package com.spring.app.user.qna.jungyj.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.qna.jungyj.domain.QnaDTO;
import com.spring.app.user.qna.jungyj.service.UserQnaService;

import jakarta.servlet.http.HttpServletRequest;
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
	
	@GetMapping("list")
	public String list(Model model,
	                   @RequestParam(value="pageno",    defaultValue="1") int currentShowPageNo, /* 현재 페이지번호 */   
	                   HttpServletRequest request) {
		
		// 원래 userseq 도 같인 넘겨줘야 함
		// 여기선 임시로 작성해 두겠음
		int userseq = 1;
		
		
		// 페이징 처리 시작
		int sizePerPage = 5;     // 한 페이지당 보여질 행의 개수
		int totalCount = 0;       // 전체 페이지 개수
		long totalDataCount = 0; // 전체 데이터의 개수
		String pageBar = "";     // 페이지바
		
		if(currentShowPageNo < 1) {
			currentShowPageNo = 1;
		}

		List<QnaDTO> pageQna = userQnaService.getPageQna(userseq, currentShowPageNo, sizePerPage);
		
		totalPage = pageQna.getTotalPages();    // 전체 페이지 개수
		
		// 총 게시물 건수 (totalCount)
		totalCount = service.getTotalCount(paraMap);
		
		
		totalPage = (int) Math.ceil( (double)totalCount/sizePerPage );
		
		if(totalPage > 0 && currentShowPageNo > totalPage) {
			currentShowPageNo = totalPage;
			pageQna = userQnaService.getPageQna(userseq, currentShowPageNo, sizePerPage);
		}
		
		totalDataCount = pageQna.getTotalElements(); // 전체 데이터의 개수

		
		List<QnaDTO> qnaList = pageQna.getContent();

	    model.addAttribute("qnaDtoList", qnaList);
	    
		 // === 페이지바 만들기 시작 === //
	    int blockSize = 10;
	    int loop = 1;
	    int pageno = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;

	    pageBar = "<ul class='pagination justify-content-center'>";
	    
	    String contextPath = request.getContextPath();
	    String url = contextPath+"/qna/list";
	    

	    // === [맨처음][이전] 만들기 === //
	    if (pageno != 1) {
	        pageBar += "<li class='page-item'><a class='page-link' href='" + url + "&pageno=1'>&laquo;</a></li>";
	        pageBar += "<li class='page-item'><a class='page-link' href='" + url + "&pageno=" + (pageno - 1) + "'>&lsaquo;</a></li>";
	    }

	    while (!(loop > blockSize || pageno > totalPage)) {
	        if (pageno == currentShowPageNo) {
	            pageBar += "<li class='page-item active'><span class='page-link'>" + pageno + "</span></li>";
	        } else {
	            pageBar += "<li class='page-item'><a class='page-link' href='" + url +  "&pageno=" + pageno + "'>" + pageno + "</a></li>";
	        }
	        loop++;
	        pageno++;
	    }

	    // === [다음][마지막] 만들기 === //
	    if (pageno <= totalPage) {
	        pageBar += "<li class='page-item'><a class='page-link' href='" + url + "&pageno=" + pageno + "'>&rsaquo;</a></li>";
	        pageBar += "<li class='page-item'><a class='page-link' href='" + url +  "&pageno=" + totalPage + "'>&raquo;</a></li>";
	    }

	    pageBar += "</ul>";
	    
	    
	    model.addAttribute("pageBar", pageBar);
	    model.addAttribute("totalDataCount", totalDataCount);
	    model.addAttribute("currentShowPageNo", currentShowPageNo);
	    model.addAttribute("sizePerPage", sizePerPage);
	    // === 페이지바 만들기 끝 === //

		
		
		return "jungyj/qna/userqnalist";
		//  src/main/resources/templates/admin/jungyj/qna/userqnalist.html 페이지를 만들어야 한다.
	}
}
