package com.spring.app.knowhow.kimkc.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.knowhow.kimkc.service.KnowhowService;

import jakarta.servlet.http.HttpServletRequest;

import com.spring.app.classify.kimkc.domain.RecipeDTO;
import com.spring.app.kimkc.entity.Recipe;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/knowhow/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class KnowhowController {

	private final KnowhowService service;
	
	
	// 분류 화면 
	@GetMapping("list")
	public ModelAndView list(ModelAndView mav,
							@RequestParam(name="pageno", defaultValue = "1") String currentShowPageNo
							,HttpServletRequest request) {
		
		// referer
		
		// 노하우 작성하는 관리자 닉네임이 "장금이의 비법" 이다.
		int sizePerPage = 4;     // 한 페이자당 보여질 행의 개수.
       	// (DB 가 Oracle 12C 이상인 경우
       	//  sizePerPage 가 FETCH NEXT 행개수 의 역할을 함.
     	//    SELECT * 
     	//    FROM employees 
     	//     ORDER BY employee_id 
     	//     OFFSET (1-1)*10 ROWS 
      	//     FETCH NEXT 10 ROWS ONLY;

		int currentShowPageNo_int = Integer.parseInt(currentShowPageNo);
		int totalPage = 0;        // 전체 페이지 개수
		long totalDataCount = 0;  // 전체 데이터의 개수
		String pageBar = "";      // 페이지바
		
		// 현재 페이지에 해당하는 노하우 리스트 불러오기
		Page<Recipe> pageKnowhow = service.getPageKnowhow(currentShowPageNo_int, sizePerPage); 
		/* Spring Data (spring-data-commons 라이브러리)에서 제공하는 인터페이스인 Page 는 페이징 처리된 데이터와 함께, 
        페이지 정보(총 페이지수, 전체 아이템 개수, 현재 페이지 번호, 현재 페이지의 아이템 등등)를 
        제공해주는 인터페이스이다.

        !!! Page 객체는 일반적으로 Repository의 조회 메소드(findAll(), findBy필드명, 등등)에서 Pageable 객체를 파라미터로 받아서 결과물을 반환할 때 사용된다. !!! 
     */
     
     /* >> Page 객체의 주요 메서드들 <<

        getContent()
        → 현재 페이지의 데이터 목록을 반환한다.
        
        getTotalElements()
        → 쿼리 결과물의 전체 데이터 개수이다. 
          즉, Pageable에 의해 limit 조건(offset 행개수 row fetch next 행개수 row only)이 들어가지 않는 쿼리 결과의 수 인데, 
          주의해야 할 점은 쿼리 결과의 *갯수*만 가져오는 것이지 전체 데이터를 가져오지 않는다는 점이다.
          이 메서드는 게시판 기능에서 사용자에게 전체 데이터 개수를 알려주는 등에 사용하기 좋다.
          [ limit(offset 행개수 row fetch next 행개수 row only) 조건은 조회할 데이터의 최대 개수를 설정하는 조건으로, 페이징 처리에서 중요한 역할을 한다. 
            limit(offset 행개수 row fetch next 행개수 row only) 조건은 데이터베이스에서 조회할 결과의 최대 개수를 제한하는 역할을 한다.
            
            Spring Data (spring-data-commons 라이브러리)에서 제공하는 인터페이스인 Pageable을 사용할 때 
            각 페이지별 보여질 목록의 개수인 sizePerPage 만 알려주면 
            limit 조건과 offset 조건이(offset 행개수 row fetch next 행개수 row only 가) 자동으로 적용된다.
          ] 

        
        getTotalPages()
        → 쿼리를 통해 가져온 요소들을 size크기(sizePerPage)에 맞춰 페이징하였을 때 나오는 총 페이지의 갯수이다.
          이를 활용해 쉽게 페이지 버튼의 생성이 가능하다.
        
        getSize()
        → 쿼리를 수행한 전체 데이터에 대해 일정 수 만큼 나눠 페이지를 구성하는데, 이 일정 수의 크기이다.
        
        getNumber()
        → 요소를 가져온 페이지의 번호(현재 페이지 번호)를 의미한다.
        
        getNumberOfElements()
        → 페이지에 존재하는 요소의 개수이다. 최대 size의 수 만큼 나올 수 있다.

      */
		
		totalPage = pageKnowhow.getTotalPages(); // 전체 페이지 개수
		System.out.println("전체 페이지 수: " + totalPage);
		
		if(totalPage > 0 && currentShowPageNo_int > totalPage) {
			currentShowPageNo_int = totalPage;
			pageKnowhow = service.getPageKnowhow(currentShowPageNo_int, sizePerPage);
		}
		
		totalDataCount = pageKnowhow.getTotalElements();
		
		List<Recipe> knowhowList = pageKnowhow.getContent();
		
		// 엔티티를 DTO로 변환
		List<RecipeDTO> knowhowDtoList = knowhowList.stream()
				.map(entity -> RecipeDTO.builder()
						.recipeseq(entity.getRecipeSeq())
						.r_subject(entity.getRSubject())
						.fk_userseq(entity.getUser().getUserseq())
						.nickname(entity.getUser().getNickname())
						.profile_sysfilename(entity.getUser().getProfileSysfilename())
						.r_imgname(entity.getRImgName())
						.r_views(entity.getRViews())
						.build() )
				.toList();
		
		
		System.out.println(knowhowDtoList.toString());
		
		mav.addObject("knowhowDtoList", knowhowDtoList);
		
		
		
		
		
		// ========== 페이지바 만들기 시작 =========== //
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
        String url = ctxPath + "/knowhow/list";
        
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
        
		
	  	// ========== 페이지바 만들기 종료 =========== //
		
		
		
		
		
		
		mav.setViewName("knowhow/kimkc/knowhow");
		//  src/main/resources/templates/knowhow/kimkc/knowhow.html 페이지를 만들어야 한다.
		return mav;
	}
}
