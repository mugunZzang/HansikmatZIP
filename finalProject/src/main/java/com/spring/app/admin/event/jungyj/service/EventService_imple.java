package com.spring.app.admin.event.jungyj.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
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
		
		List<Event> eventList = eventRepository.findAll(Sort.by(Sort.Direction.DESC, "eventseq"));
		
		return eventList.stream()
	            .map(event -> new EventDTO(event)) // event -> new EventDTO(event) 와 동일
	            .toList();
	}
	
	
	//============================================= 이벤트 목록 조회 ===============================================
	
	
	
	
	
	// 첨부파일 업로드 해주는 함수 
	private String updateFile(byte[] bytes, String originalFilename, String path) {
		
		String fileName = "";
		try {
			fileName = fileManager.doFileUpload(bytes, originalFilename, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fileName;
	}

	
}
