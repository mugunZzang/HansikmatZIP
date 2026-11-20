package com.spring.app.admin.event.jungyj.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.spring.app.common.FileManager;

import lombok.RequiredArgsConstructor;

//==== Service 선언 ==== //
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단

@Service
@RequiredArgsConstructor
public class EventService_imple implements EventService {

	// === 파일업로드 및 파일다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection) === 
	private final FileManager fileManager;
	
	@Value("${file.eventimageimage-dir}")
	private String eventimageimageDir; // 이벤트 등록시 파일 경로 잡아주는 것
	
	
}
