package com.spring.app.knowhow.kimkc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.knowhow.kimkc.service.KnowhowService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/knowhow/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
public class KnowhowController {

	private final KnowhowService service;
	
	
	// 분류 화면 
	@GetMapping("list")
	public ModelAndView list(ModelAndView mav) {
		
		// referer
		
		// 노하우 작성하는 관리자 닉네임이 "장금이의 비법" 이다.
		int sizePerPage = 5;     // 한 페이자당 보여질 행의 개수.
       	// (DB 가 Oracle 12C 이상인 경우
       	//  sizePerPage 가 FETCH NEXT 행개수 의 역할을 함.
     	//    SELECT * 
     	//    FROM employees 
     	//     ORDER BY employee_id 
     	//     OFFSET (1-1)*10 ROWS 
      	//     FETCH NEXT 10 ROWS ONLY;

		int totalPage = 0;        // 전체 페이지 개수
		long totalDataCount = 0;  // 전체 데이터의 개수
		String pageBar = "";      // 페이지바
		
		//Page<Board> pageBoard = boardService.getPageBoard(searchType, searchWord, currentShowPageNo, sizePerPage); 
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
		
		//totalPage = pageBoard.getTotalPages(); // 전체 페이지 개수
		
		
		
		mav.setViewName("knowhow/kimkc/knowhow");
		//  src/main/resources/templates/knowhow/kimkc/knowhow.html 페이지를 만들어야 한다.
		return mav;
	}
}
