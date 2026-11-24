package com.spring.app.classify.kimkc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.classify.kimkc.domain.RecipeDTO;
import com.spring.app.classify.kimkc.service.ClassifyService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/classify/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class ClassifyController {

	
	private final ClassifyService service;
	
	// 분류 화면 
	@GetMapping("list")
	public ModelAndView list(ModelAndView mav,
							@RequestParam Map<String, String> map,
							@RequestParam(name="pageno", defaultValue = "1") String currentShowPageNo,
							HttpServletRequest request) {
		
		// referer
		/*
		 * String referer = request.getHeader("referer"); if(referer == null) {
		 * mav.setViewName("redirect:/index"); return mav; }
		 */
		/*
		System.out.println("종류별:" + map.get("cf_type"));
		System.out.println("주재료별:" + map.get("cf_main"));
		System.out.println("상황별:" + map.get("cf_situation"));
		System.out.println("방법별:" + map.get("cf_recipe"));
		System.out.println("조리시간:" + map.get("cf_time"));
		System.out.println("난이도:" + map.get("cf_level"));
		System.out.println("인원수:" + map.get("cf_people"));
		System.out.println("멤버십:" + map.get("cf_membership"));
		System.out.println("정렬기준:" + map.get("cf_sort"));
		*/
		int sizePerPage = 4;		// 한 페이지당 20개
		int totalPage = 0;			// 검색한 레시피 총 페이지수
		long totalDataCount = 0;	// 웹에 뿌려줄 검색 레시피 총 개수
		
		
		map.put("currentShowPageNo", currentShowPageNo);
		map.put("sizePerPage", String.valueOf(sizePerPage));

		int currentShowPageNo_int = Integer.parseInt(currentShowPageNo);
		String pageBar = "";
		
		
		
		// 검색 조건에 맞는 총 레시피 수 알아오기
		totalDataCount = service.getTotalDataCount(map);
		System.out.println("토탈:"+totalDataCount);
		mav.addObject("totalDataCount",totalDataCount);
		
		// 총 페이지 수 구하기
		totalPage = (int) Math.ceil( (double)totalDataCount / sizePerPage );
		
		
		// 해당 페이지의 레시피 목록 갖고오기
		List<RecipeDTO> recipeList = service.getRecipeList(map);
		for(RecipeDTO dto : recipeList) {
			System.out.println("레시피번호:" + dto.getRecipeseq());
			System.out.println("레시피명:" +dto.getR_subject());
			System.out.println("작성자:" + dto.getNickname());
		}
		mav.addObject("recipeList", recipeList);
		mav.addObject("resultMap", map);
		
		
		
		
		
		// ============= 페이지바 만들기 ================//
		int blockSize = 10;
        // blockSize 는 1개 블럭(토막)당 보여지는 페이지번호의 개수이다.
        /*
                        1  2  3  4  5  6  7  8  9 10 [다음][마지막]  -- 1개블럭
           [맨처음][이전]  11 12 13 14 15 16 17 18 19 20 [다음][마지막]  -- 1개블럭
           [맨처음][이전]  21 22 23
        */
        
        int loop = 1;
        /*
            loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수[ 지금은 10개(== blockSize) ] 까지만 증가하는 용도이다.
         */
        
        int pageno = ((currentShowPageNo_int - 1)/blockSize) * blockSize + 1;
        // *** !! 공식이다. !! *** //
        
     /*
         1  2  3  4  5  6  7  8  9  10  -- 첫번째 블럭의 페이지번호 시작값(pageno)은 1 이다.
         11 12 13 14 15 16 17 18 19 20  -- 두번째 블럭의 페이지번호 시작값(pageno)은 11 이다.
         21 22 23 24 25 26 27 28 29 30  -- 세번째 블럭의 페이지번호 시작값(pageno)은 21 이다.
         
         currentShowPageNo         pageno
        ----------------------------------
              1                      1 = ((1 - 1)/10) * 10 + 1
              2                      1 = ((2 - 1)/10) * 10 + 1
              3                      1 = ((3 - 1)/10) * 10 + 1
              4                      1
              5                      1
              6                      1
              7                      1 
              8                      1
              9                      1
              10                     1 = ((10 - 1)/10) * 10 + 1
             
              11                    11 = ((11 - 1)/10) * 10 + 1
              12                    11 = ((12 - 1)/10) * 10 + 1
              13                    11 = ((13 - 1)/10) * 10 + 1
              14                    11
              15                    11
              16                    11
              17                    11
              18                    11 
              19                    11 
              20                    11 = ((20 - 1)/10) * 10 + 1
              
              21                    21 = ((21 - 1)/10) * 10 + 1
              22                    21 = ((22 - 1)/10) * 10 + 1
              23                    21 = ((23 - 1)/10) * 10 + 1
              ..                    ..
              29                    21
              30                    21 = ((30 - 1)/10) * 10 + 1
     */
        
        pageBar = "<ul style='list-style:none;'>";
        String ctxPath = request.getContextPath();
        String url = ctxPath + "/classify/list";
        
        // === [맨처음][이전] 만들기 === //	
        if(pageno != 1) {
           pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?pageno=1'>[맨처음]</a></li>";
           pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?pageno="+(pageno-1)+"'>[이전]</a></li>"; 
        }
        
        while( !(loop > blockSize || pageno > totalPage) ) {
           
           if(pageno == currentShowPageNo_int) {
              pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; color:red; padding:2px 4px;'>"+pageno+"</li>";
           }
           else {
              pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?pageno="+pageno+"'>"+pageno+"</a></li>"; 
           }
           
           loop++;
           pageno++;
        }// end of while------------------------
        
        // === [다음][마지막] 만들기 === //
        if(pageno <= totalPage) {
           pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?pageno="+pageno+"'>[다음]</a></li>";
           pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?pageno="+totalPage+"'>[마지막]</a></li>"; 
        }
        
        pageBar += "</ul>";
        
        mav.addObject("pageBar", pageBar);
		
		
        // ============= 페이지바 만들기 끝 ================//
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		mav.setViewName("classify/kimkc/recipe_classify");
		//  src/main/resources/templates/classify/kimkc/recipe_classify.html 페이지를 만들어야 한다.
		return mav;
	}
	
	
	
	
	
	
	
}
