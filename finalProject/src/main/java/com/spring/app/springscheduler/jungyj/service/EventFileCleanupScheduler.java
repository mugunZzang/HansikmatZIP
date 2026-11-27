package com.spring.app.springscheduler.jungyj.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spring.app.admin.event.jungyj.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventFileCleanupScheduler {

	private final EventRepository eventRepository;
    private final FileCleanupHelper fileCleanupHelper;
    
	@Value("${file.eventimageimage-dir}")
	private String eventimageimageDir; // 이벤트 등록시 파일 경로 잡아주는 것
	
	// 매일 새벽 3시 실행 (초 분 시 일 월 요일)
	// DB에 존재하지 않는 고아 파일 제거 스케줄러
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupOrphanEventImages() {

        
/*
        Set(집합)이란, 데이터 자료구조(데이터 컬렉션) 중에 하나로 특정한 값들을 저장하는 추상자료형을 뜻한다.
	    값들을 중복으로 삽입이 불가능하다. 동일한 값이 삽입되면 하나의 값만 저장된다.
        데이터를 비순차적으로 저장할 수 있는 순열 자료구조이다.
        
        usedFilenames는 HashSet이라는 클래스로 만든 객체를 가리키는 변수
        
        Set의 주요 메서드
		add(element): Set에 요소를 추가.
		remove(element): Set에서 요소를 제거.
		contains(element): Set에 특정 요소가 있는지 확인.
		size(): Set의 요소 개수를 반환.
*/
   
    	Set<String> usedFilenames = new HashSet<>(eventRepository.findAllUsedImageFilenames());
        fileCleanupHelper.deleteOrphanFiles(eventimageimageDir, usedFilenames, "Event");
    }
    

    
}
