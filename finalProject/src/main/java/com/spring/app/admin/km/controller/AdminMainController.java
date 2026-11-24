package com.spring.app.admin.km.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.app.admin.km.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/km/admin/")
public class AdminMainController {

	private final AdminService service;
	
	// @PreAuthorize("hasRole('ADMIN')")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("admin_index")   
    public String index() {
    	
    	
		return "km/admin/admin_index";
    }// end of public String index() {}
	
	
	// @PreAuthorize("hasRole('ADMIN')")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("todaymembership") 
	@ResponseBody
    public Map<String,Integer> todaymembership() {
		
		int n = service.todaymembership();
    	
    	Map<String, Integer> map = new HashMap<>();
    	map.put("count", n);
    	
    	return map;
    }// end of public Map<String,Integer> todaymembership() {
	
	// @PreAuthorize("hasRole('ADMIN')")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("todaywriterecipe") 
	@ResponseBody
    public Map<String,Integer> todaywriterecipe() {
		
		int n = service.todaywriterecipe();
    	
    	Map<String, Integer> map = new HashMap<>();
    	map.put("count", n);
    	
    	return map;
    }// end of public Map<String,Integer> todaymembership() {
	
	// @PreAuthorize("hasRole('ADMIN')")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("todaynewchef") 
	@ResponseBody
    public Map<String,Integer> todaynewchef() {
		
		int n = service.todaynewchef();
    	
    	Map<String, Integer> map = new HashMap<>();
    	map.put("count", n);
    	
    	return map;
    }// end of public Map<String,Integer> todaymembership() {
	
	// @PreAuthorize("hasRole('ADMIN')")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("todayqna") 
	@ResponseBody
    public List<Map<String,Object>> todayqna() {
		
		List<Map<String,Object>> mapList = service.todayqna();	
    	
    	return mapList;
    }// end of public List<Map<String,String>> todayqna() {

	// @PreAuthorize("hasRole('ADMIN')")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("endevent") 
	@ResponseBody
    public List<Map<String,Object>> endevent() {
		
		List<Map<String,Object>> mapList = service.endevent();	
    	
    	return mapList;
    }// end of public List<Map<String,String>> endevent() {
	
	// 검수하기 페이지
	// @PreAuthorize("hasRole('ADMIN')")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("reveal") 
    public String reveal(Model model,
    					 @RequestParam(name="currentShowPageNo", defaultValue ="1") String currentShowPageNo,
    					 HttpServletRequest request) {
    	
		int totalCount = 0;       // 총 게시물 건수
		int sizePerPage = 4;     // 한 페이지당 보여줄 게시물 건수
		int totalPage = 0;        // 총 페이지 수 (웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		
		// 총 검수 할 레시피 건수 (totalCount)
		totalCount = service.getTotalCount();
		
		totalPage = (int) Math.ceil( (double)totalCount/sizePerPage );
		
		List<Map<String,Object>> revealList = service.revealrecipe(currentShowPageNo);
		
		// ============================================= 페이지바 만들기 =============================================== //
		int blockSize = 10;
		// blockSize 는 1개 블럭(토막)당 보여지는 페이지번호의 개수이다.
       /*
                      1  2  3  4  5  6  7  8  9 10 [다음][마지막]  -- 1개블럭
         [맨처음][이전]  11 12 13 14 15 16 17 18 19 20 [다음][마지막]  -- 1개블럭
         [맨처음][이전]  21 22 23
       */
		
		int loop = 1;
		// loop 는 1 부터 증가하여 1개 블럭을 이루는 페이지번호의 개수(지금은 10개)까지만 증가하는 용도이다.

		// ==== !!! 다음은 pageNo 구하는 공식이다. !!! ==== // 
        int pageNo  = ( (Integer.parseInt(currentShowPageNo) - 1)/blockSize ) * blockSize + 1; 

        String pageBar = "<ul style='list-style:none;'>";
        String url = "reveal";
        // *** [맨처음][이전] 만들기 *** //
    	pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?currentShowPageNo="+sizePerPage+"' >[맨처음]</a></li>";
        if(pageNo != 1) {
        	pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?currentShowPageNo="+(pageNo-1)+"' >[이전]</a></li>";
        }

        
        while( !(loop > blockSize || pageNo > totalPage)) {
        	
        	if(pageNo == Integer.parseInt(currentShowPageNo)) {
	        	pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; color:red; padding:2px 4px;'>" + pageNo + "</li>";
        	} else {
        		pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='" + url + "?currentShowPageNo="+pageNo+"' >" + pageNo + "</a></li>";
        	
        	}
        	loop++;   // 1  2  3  4  5  6  7  8  9  10
        	
        	pageNo++; // 1  2  3  4  5  6  7  8  9  10
        	          // 11 12 13 14 15 16 17 18 19 20
        	          // 21 22 23 24 25 26 27 28 29 30
        	          // 31 32 33 34 35 36 37 38 39 40
        			  // 41 42 
        } // end of while( !(loop > 10) || pageNo > totalPage)---------------------------------
        
        // === [다음][마지막] 만들기
        if(pageNo <= totalPage) {
        	pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?currentShowPageNo="+pageNo+"' >[다음]</a></li>";
        }
    	pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?currentShowPageNo="+totalPage+"' >[마지막]</a></li>";

    	

		request.setAttribute("pageBar", pageBar);

        
        
        pageBar += "</ul>";
		
		model.addAttribute("pageBar", pageBar);
		
		
		
		// ============================================= 페이지바 만들기 끝 =============================================== //
		
		model.addAttribute("revealList",revealList);
		
    	return "km/admin/reveal";
    }// end of public String reveal() { {
	
	
	
}
