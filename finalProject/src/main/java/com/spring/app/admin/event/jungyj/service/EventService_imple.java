package com.spring.app.admin.event.jungyj.service;


import static com.spring.app.entity.jungyj.QEvent.event;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.app.admin.event.jungyj.domain.EventDTO;
import com.spring.app.admin.event.jungyj.domain.EventEntryDTO;
import com.spring.app.admin.event.jungyj.repository.EntryRepository;
import com.spring.app.admin.event.jungyj.repository.EventRepository;
import com.spring.app.common.FileManager;
import com.spring.app.entity.jungyj.Entry;
import com.spring.app.entity.jungyj.Event;


import lombok.RequiredArgsConstructor;

//==== Service 선언 ==== //
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단

/*
    이벤트 등록
    - 고아 파일을 삭제하기 위한 스케줄링 배치하기
	- 이미지는 업로드 되었지만 만약 DB에 저장되지 않았더라면 해당 파일들은 삭제해야 한다.
	
	

*/


@Service
@RequiredArgsConstructor
public class EventService_imple implements EventService {
	
	private final EventRepository eventRepository;
	private final EntryRepository entryRepository;
	
	private final JPAQueryFactory jPAQueryFactory;
	
	// === 파일업로드 및 파일다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection) === 
	private final FileManager fileManager;
	
	@Value("${file.eventimageimage-dir}")
	private String eventimageimageDir; // 이벤트 등록시 파일 경로 잡아주는 것

	
	
	//============================================= 이벤트 등록 ==================================================
	
	
	
	@Override
	@Transactional
	public void eventRegister(EventDTO eventDto, MultipartFile multipartFile) throws Exception {		
		// 1. 서버에 파일 저장 및 DTO에 파일명 세팅
	    if (multipartFile != null && !multipartFile.isEmpty()) {
	        String serverFileName = updateFile(multipartFile.getBytes(), multipartFile.getOriginalFilename(), eventimageimageDir);
	        eventDto.setEvent_image_org_filename(multipartFile.getOriginalFilename());
	        eventDto.setEvent_image_filename(serverFileName);
	    }
	    
	    // 2. DTO -> Entity 변환 후 DB 저장
	    Event event = eventDto.toEntity();
	    eventRepository.save(event);
		
	}

	
	//============================================= 이벤트 목록 조회 ===============================================
	
	
	
	// 이벤트 List 조회(페이징 처리)
	@Override
	public Page<EventDTO> getPageEvent(String status, int currentShowPageNo, int sizePerPage) {
		Page<EventDTO> page = Page.empty();      // 기본값으로 내용이 없는 빈 페이지. null이 아니기에 안전함
		try {
			// 1. Spring Data (spring-data-commons 라이브러리)에서 제공하는 인터페이스인 Pageable 객체 생성하기
			
			
			Pageable pageable = PageRequest.of(currentShowPageNo-1, sizePerPage, Sort.by(Sort.Direction.DESC, "eventseq"));
	/*                                         1페이지 = 0 이기때문에 항상 -1로 해야함
	        Spring Data (spring-data-commons 라이브러리)에서 제공하는 인터페이스인 Pageable 은 
	        "데이터를 페이지 단위로 검색"하고 제어하는 "인터페이스" 이다.
	        
	                                                                          
	        Pageable 을 구현한 클래스인 PageRequest 의 static 메서드인 PageRequest.of(페이지번호, 한 페이지당 보여줄 행개수, 정렬기준); 메서드를 사용하여 객체를 생성하고,
	        이 객체를 Pageable 로 받아온다.
	        
	        생성된 Pageable pageable 객체에는 원하는 해당 페이지의 데이터들이 들어있다.   
	        그런데 '페이지 번호' 의 시작은 0 부터 시작한다.!!
	          
	        그리고 한 페이지당 보여줄 행개수(각 페이지별 보여질 목록의 개수)인 sizePerPage 만 알려주면 
	        Spring Data (spring-data-commons 라이브러리)에서 제공하는 인터페이스인 Pageable 은 
	        (OFFSET 행개수 ROW FETCH NEXT 행개수 ROW ONLY) 가 자동으로 적용되어 DB에서 데이터를 가져온다.
	        
	        짧게 말하면 "페이지번호, 한 페이지당 보여줄 행개수, 정렬기준" 설정을 통해 원하는 해당 페이지의 데이터를 가져온다.          
	*/
			
			
			// 2. Repository의 조회 메서드에 파라미터로 Pageable 객체를 넣어서 페이징된 데이터를 가져오기
	        
	        //   >>> BooleanExpression은 QueryDSL 에서 제공해주는 클래스 이다.
	        //       BooleanExpression 클래스는 QueryDSL 전용의 SQL의 WHERE 조건 표현 객체로서 QueryDSL의 .where(), .and(), .or() 에만 사용된다. <<<
			
			BooleanExpression condition = Expressions.TRUE; 
	        // Expressions.TRUE 라고 준것은 기본 조건 (항상 참)으로 시작해서 조건을 점진적으로 추가한다. 마치 WHERE 1=1 과 같은 뜻이다.

			
			// 진행 중 / 종료됨 필터 
			LocalDate today = LocalDate.now();

			if ("ended".equals(status)) {
				condition = condition.and(event.eventDrawingdate.lt(today));
	        } else {
	        	// 오늘 일자보다 종료일이 더 큰가
	        	// ex) 오늘일자  (2026-11-24) <= 종료일 (2026-11-27) 아직 진행중이라는 뜻
	            condition = condition.and(event.eventEnddate.goe(today));
	        }
			
			
			List<Event> events = jPAQueryFactory
									.selectFrom(event)                       // Event 엔티티를 기준으로 조회
									.where(condition)                        // 조건절 (BooleanExpression 을 사용하여 동적 조건 처리)  
									.offset(pageable.getOffset())            // 페이지 시작 위치 (예: 0, 10, 20...).  pageable.getOffset() 은 Spring Data JPA에서 페이징 처리 시 데이터베이스에서 조회를 시작할 위치를 말하는 것이다.
									.limit(pageable.getPageSize())           // 한 페이지당 데이터 수
									.orderBy(event.eventseq.desc())          // 정렬 기준 지정: eventseq 컬럼 기준 내림차순 
									.fetch();                                // 최종적으로 리스트 형태로 결과 반환
	/*
	        pageable 은 위에서 만든 Pageable pageable = PageRequest.of(currentShowPageNo-1, sizePerPage, Sort.by(Sort.Direction.DESC, "num")); 이다. 
	        pageable.getOffset(); 의 계산방식  offset = (currentShowPageNo-1) * sizePerPage; 이다.
	        예를 들어, sizePerPage 가 10 일때
	        currentShowPageNo 가 1 이면 offset 은 0, 
	        currentShowPageNo 가 2 이면 offset 은 10, 
	        currentShowPageNo 가 3 이면 offset 은 20 이 된다. 
	*/
			
			
			// 특정 조건에 맞는 게시글의 총 개수를 조회
	        Long total = jPAQueryFactory
			                .select(event.count())
			                .from(event)
			                .where(condition)
			                .fetchOne(); // 단일 결과를 반환한다. count 는 하나의 숫자만 반환되므로 fetchOne()을 사용한다.
	        /*
	          SQL 로 설명하면
	          
	          SELECT COUNT(*)
	            FROM board JOIN member 
	            ON board.fk_user_id = member.user_id
	            WHERE [condition] 
	            
	            이다.
	         */
				
	        List<EventDTO> eventdtoList = events.stream()
	                .map(EventDTO::new)
	                .toList();
	        
	        // 신청건수 dto에 저장하기 위한 반복문
	        for (EventDTO dto : eventdtoList) {
	            long count = entryRepository.countByFkEventseq(dto.getEventseq());
	            dto.setEntryCount(count);
	        } // end of for-----------------------------------------------------------
	        

				
	/*
	            join(board.member, member) 와  join(board.member, member).fetchJoin() 의 차이점
	            
	            --------------------------------------------------------------------------------------
	             join(board.member, member)    VS   join(board.member, member).fetchJoin()
	            --------------------------------------------------------------------------------------
	             1. 주 엔티티만 영속화                        1. 주 엔티티 + 연관 엔티티 모두 영속화
	             2. LAZY(지연) 로딩 (추가 쿼리 발생)           2. EAGER(즉시) 로딩 (추가 쿼리 없음)
	             3. Board 는 영속성 컨텍스트에 저장됨           3. Board 와 Member 모두 영속성 컨텍스트에 저장됨
	                Member 는 프록시 객체로 남아 있음             Member 는 즉시 로딩됨 → 추가 쿼리 없이 바로 접근 가능   
	                board.getMember().getName() 호출 시 
	                추가 쿼리 발생
	      
	             **영속화(Persistence)**란? 엔티티 객체가 JPA의 영속성 컨텍스트에 등록되어 "관리되는 상태"를 말한다.
	             "영속성 컨텍스트" 는 DB와 애플리케이션 사이에서 객체의 상태를 추적하고 관리하는 논리적인 영역이다.
	                
	             !!! 언제 fetchJoin() 을 사용해야 하는가? ==> 연관 엔티티의 데이터를 즉시 사용해야 할 때 !!!
	*/
	        long totalCount = (total != null) ? total : 0L;
	        page = new PageImpl<>(eventdtoList, pageable, totalCount);
	        // Long total 값이 null 이 될 수 있으므로 total != null ? total : 0 으로 해주었다.
	        
	        /*
	            PageImpl 이란? 
	            Spring Data 에서 제공하는 "페이징 처리 결과" 를 담는 기본 구현 클래스이다.
	              Page<T> 인터페이스를 구현하며, 페이징된 데이터와 관련 정보를 함께 제공해준다. 
	              간단히 말하자면 페이징된 결과를 담기 위한 용도로 쓰이는 객체이다.
	              
	              생성자는 new PageImpl<>(contentList, pageable, totalCount); 이며
	              첫번째 파라미터인 contentList 는 현재 페이지에 해당하는 데이터 목록이 들어오며,
	              두번째 파라미터인 pageable 에는 요청된 페이지 정보(page, size, sort 등)가 들어오며,
	              세번째 파라미터인 totalCount 에는 조건에 따른 전체 데이터 개수가 들어온다. 
	         */
			
		} catch(Exception e) {
        	e.printStackTrace();
        }
		return page;

	}

	
	// 진행 중인 이벤트 개수
	public int getOngoingCount() {
		return eventRepository.countByEventDrawingdateGreaterThanEqual(LocalDate.now());
	}

	// 종료된 이벤트 개수
	public int getEndedCount() {
		return eventRepository.countByEventDrawingdateLessThan(LocalDate.now());
	}
	
	// 상단 고정용: 당첨 선정 대기 목록 조회 (종료일 < 오늘 <= 발표일)
	@Override
	public List<EventDTO> getDrawingList() {
		// 
	    List<Event> events = eventRepository.findDrawingEvents(LocalDate.now());

	    List<EventDTO> dtoList = events.stream()
	            .map(EventDTO::new)
	            .toList();

	    for (EventDTO dto : dtoList) {
	        long count = entryRepository.countByFkEventseq(dto.getEventseq());
	        dto.setEntryCount(count);
	    }

	    return dtoList;
	}
	
	
	//============================================== 이벤트 상세 페이지 =============================================

	
	
	// 이벤트 1건 조회
	@Override
	public EventDTO detail(int eventseq) {
		return getEventEntity(eventseq).toDTO();
	}
	
	@Transactional
	@Override
	public void winEntries(int eventseq, List<Integer> entryseqs) {
	    Event event = getEventEntity(eventseq);

	    // 이미 당첨된 인원 + 이번에 선택한 인원이 당첨 정원을 초과하는지 체크
	    long currentWinners = entryRepository.countByFkEventseqAndChkWin(eventseq, 1);
	    
	    // 아직 미당첨(chkWin == 0)인 항목을 추려낸 이유
	    // 여러명의 관리자가 한 페이지를 보고있을 때
	    // 누군가 먼저 당첨처리를 하고 다른 관리자가 새로고침을 하지 않고 이벤트를 발생시켰을 때
	    // 문제가 발생할 수 있음
	    List<Entry> targetEntries = entryRepository.findByEntryseqInAndFkEventseqAndChkWin(entryseqs, eventseq, 0);

	    
	    if (currentWinners + targetEntries.size() > event.getEventTotalWinners()) {
	        throw new IllegalArgumentException(
	            "당첨 정원(" + event.getEventTotalWinners() + "명)을 초과할 수 없습니다. (현재 당첨: " + currentWinners + "명)"
	        );
	    }

	    // 당첨 처리 (@Transactional 변경 감지로 자동 UPDATE)
	    for (Entry entry : targetEntries) {
	        entry.setChkWin(1);
	    }
	}
	
	
	// 이벤트 참가 레시피 목록 조회
	@Override
	public List<EventEntryDTO> getEntryList(int eventseq) {
		List<Entry> entries = entryRepository.findEntriesByEventseq(eventseq);

		List<EventEntryDTO> dtoList = entries.stream()
		                                     .map(entity -> EventEntryDTO.builder()
		                                    		                     .entryseq(entity.getEntryseq())
		                                    		                     .nickname(entity.getEventUser().getNickname())
		                                    		                     .r_subject(entity.getEventRecipe().getRSubject())
		                                    		                     .fk_eventseq(entity.getFkEventseq())
		                                    		                     .fk_userseq(entity.getFkUserseq())
		                                    		                     .fk_recipeseq(entity.getFkRecipeseq())
		                                    		                     .chk_win(entity.getChkWin())
		                                    		                     .build()
		                                          )
		                                     .toList();
	    return dtoList;
	}
	
	
	//================================================ 이벤트 수정 =================================================

	
	
	// 이벤트 수정하기
	// JPA의 영속상태를 이용한 Dirty Checking
	@Override
	@Transactional
	public void eventEdit(EventDTO eventDto, MultipartFile multipartFile) throws Exception{
		Event originEvent = getEventEntity(eventDto.getEventseq());  // 영속성 객체 조회

		// 삭제 예정인 파일명 저장
		final String oldServerFilename = originEvent.getEventImageFilename();
		boolean isFileChanged = false; // 파일이 실제로 교체되었는지 체크
		
		// 1. 파일이 사용자가 올린 파일과 동일한지 다른지 검증
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String newOrgFilename = multipartFile.getOriginalFilename();
			
			
			// 1-1. 파일명이 다르다면 기존 파일 먼저 저장
			if ( !newOrgFilename.equals(originEvent.getEventImageOrgFilename()) ) {
		        String serverFileName = updateFile(multipartFile.getBytes(), multipartFile.getOriginalFilename(), eventimageimageDir);
		        
		        
		        originEvent.setEventImageOrgFilename(newOrgFilename);  // 객체 필드 변화
		        originEvent.setEventImageFilename(serverFileName);     // 객체 필드 변화
		        isFileChanged = true;
			}
			
			// 1-2 동일한 파일의 경우 저장 과정 Skip
			
		}
		
	    // 2. DB 데이터 수정  
		originEvent.setEventSubject(eventDto.getEvent_subject());     // 객체 필드 변화
	    originEvent.setEventStartdate(eventDto.getEvent_startdate()); // 객체 필드 변화
	    originEvent.setEventEnddate(eventDto.getEvent_enddate());     // 객체 필드 변화
	    originEvent.setEventDrawingdate(eventDto.getEvent_enddate().plusDays(2)); // 객체 필드 변화
	    originEvent.setEventTotalWinners(eventDto.getEvent_total_winners()); // 객체 필드 변화
	    originEvent.setEventContents(eventDto.getEvent_contents()); // 객체 필드 변화
	    
	    // 3. 실제 DB UPDATE를 여기서 즉시 반영 (오류 시 아래 deleteFile이 실행되지 않음)
	    eventRepository.flush();
	    
	    // 4. 파일이 실제로 교체되었고 DB 반영까지 성공했을 때만 기존 파일 삭제
	    if (isFileChanged && oldServerFilename != null) {
	        deleteFile(oldServerFilename, eventimageimageDir);
	    }
		
	    // eventEdit 함수가 종료되면서 @Transaction이 사용자가 조회한 기존 영속성 객체와 필드를 비교함 
	    // 만약 필드가 달라졌다면 달라진 것들은 DB에서 UPDATE 진행
	    // 이것을 Dirty Checking 이라 부름
	}

	
	
	
	// =========================== 공통 조회 메서드 (Service 내부용) =======================================
	
	
	private Event getEventEntity(int eventseq) {
	    return eventRepository.findById(eventseq)
	            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트입니다. eventseq=" + eventseq));
	} // end of private Event getEventEntity(int eventseq)--------------------------

	
	// 첨부파일 업로드 해주는 함수 
	private String updateFile(byte[] bytes, String originalFilename, String path) {
		
		String fileName = "";
		try {
			fileName = fileManager.doFileUpload(bytes, originalFilename, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fileName;
	} // end of private String updateFile(byte[] bytes, String originalFilename, String path)---------------

	
	// 파일 삭제하기
	private void deleteFile(String fileName, String filePath) {
    	if(fileName != null && !fileName.isBlank()) {
    		try {
				fileManager.doFileDelete(fileName, filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
	} // end of private void deleteFile(String fileName, String filePath)-----------------------------




	
}
