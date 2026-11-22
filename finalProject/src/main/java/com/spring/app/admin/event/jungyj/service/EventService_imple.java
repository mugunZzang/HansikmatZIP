package com.spring.app.admin.event.jungyj.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.app.admin.event.jungyj.domain.EventDTO;
import com.spring.app.admin.event.jungyj.repository.EventRepository;
import com.spring.app.common.FileManager;
import com.spring.app.entity.jungyj.Event;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

//==== Service 선언 ==== //
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단

@Service
@RequiredArgsConstructor
public class EventService_imple implements EventService {

	private final EventRepository eventRepository;
	
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
	

	@Override
	public List<EventDTO> list(Map<String, String> paraMap) {
		
		String status = paraMap.get("status");
		LocalDate today = LocalDate.now();
		
		List<Event> eventList;
		if("ended".equals(status)) {
			eventList = eventRepository.findByEventEnddateLessThanOrderByEventseqDesc(today);
		} else {
			eventList = eventRepository.findByEventEnddateGreaterThanEqualOrderByEventseqDesc(today);
		}
		
		return eventList.stream()
	            .map(event -> new EventDTO(event)) // event -> new EventDTO(event) 와 동일
	            .toList();
	}
	
	// 진행 중인 이벤트 개수
	public int getOngoingCount() {
	    return eventRepository.countByEventEnddateGreaterThanEqual(LocalDate.now());
	}

	// 종료된 이벤트 개수
	public int getEndedCount() {
	    return eventRepository.countByEventEnddateLessThan(LocalDate.now());
	}
		
	// 이벤트 1건 조회
	@Override
	public EventDTO detail(int eventseq) {
		return getEventEntity(eventseq).toDTO();
	}
	
	// 이벤트 수정하기
	// JPA의 영속상태를 이용한 Dirty Checking
	@Override
	@Transactional
	public void eventEdit(EventDTO eventDto, MultipartFile multipartFile) throws Exception{
		Event originEvent = getEventEntity(eventDto.getEventseq());  // 영속성 객체 조회
		
		// 1. 파일이 사용자가 올린 파일과 동일한지 다른지 검증
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String newOrgFilename = multipartFile.getOriginalFilename();
			
			// 1-1. 파일명이 다르다면 기존 파일 삭제 후 저장
			if ( !newOrgFilename.equals(originEvent.getEventImageOrgFilename()) ) {
				deleteFile(originEvent.getEventImageFilename(), eventimageimageDir);
				
		        String serverFileName = updateFile(multipartFile.getBytes(), multipartFile.getOriginalFilename(), eventimageimageDir);
		        
		        
		        originEvent.setEventImageOrgFilename(newOrgFilename);  // 객체 필드 변화
		        originEvent.setEventImageFilename(serverFileName);     // 객체 필드 변화
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
	    
	    // eventEdit 함수가 종료되면서 @Transaction이 사용자가 조회한 기존 영속성 객체와 필드를 비교함 
	    // 만약 필드가 달라졌다면 달라진 것들은 DB에서 UPDATE 진행
	    // 이것을 Dirty Checking 이라 부름
	}

	
	
	//============================================= 이벤트 목록 조회 ===============================================
	
	// 공통 조회 메서드 (Service 내부용)
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
	} // end of private void deleteFile(String fileName, String filePath)--------------------------

	
}
