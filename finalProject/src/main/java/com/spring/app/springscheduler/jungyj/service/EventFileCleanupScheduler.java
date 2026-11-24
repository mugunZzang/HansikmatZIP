package com.spring.app.springscheduler.jungyj.service;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spring.app.admin.event.jungyj.repository.EventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventFileCleanupScheduler {

	private final EventRepository eventRepository;
	
	@Value("${file.eventimageimage-dir}")
	private String eventimageimageDir; // 이벤트 등록시 파일 경로 잡아주는 것
	
	// 매일 새벽 3시 실행 (초 분 시 일 월 요일)
	// DB에 존재하지 않는 고아 파일 제거 스케줄러
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupOrphanEventImages() {
    	File dir = new File(eventimageimageDir);
    	
    	// 폴더가 존재하지 않는다면 종료
    	if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

    	// 폴더에 파일이 없을 경우 종료 
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        
     // 1. DB에서 사용 중인 파일명 조회 (Set으로 변환해 검색 속도 향상, 중복된 것은 건너뛰기) → 사실 밀리초로 제목을 만들기에 중복은 될 수 없음
        Set<String> usedFilenames = new HashSet<>(eventRepository.findAllUsedImageFilenames());
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
        
     // 2. 유예 시간 기준 (현재 시간 - 1시간)
        long oneHourAgo = System.currentTimeMillis() - (60 * 60 * 1000L);
        int deletedCount = 0;
        
     // 3. 폴더 내 파일 검사 및 고아 파일 삭제
        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }

            // 진짜 우연히 오전 3시에 등록을 했는데 DB Insert가 반영되지 않았을 때
            // 해당 파일이 삭제되지 않게 유예 시간 1시간 준 것
            boolean isOldEnough = file.lastModified() < oneHourAgo;
            
            // set에서의 contains 는 대소문자를 구분하여 문자열이 일치하는지 확인
            // contains(element): Set에 특정 요소가 있는지 확인.
            // 특정 요소 존재하면 True 없다면 False
            // DB에 존재하는 파일은 삭제하면 안되기에
            // DB 파일명 AND 서버 폴더에 파일명 일치 하는 경우 False
            // DB 파일명 AND 서버 폴더에 파일명 일치 하지 않는 경우 True
            boolean isOrphan = !usedFilenames.contains(file.getName());

            if (isOldEnough && isOrphan) {
                if (file.delete()) {
                    deletedCount++;
                    log.info("고아 파일 삭제 완료: {}", file.getName());
                } else {
                    log.warn("고아 파일 삭제 실패: {}", file.getName());
                }
            }
        }

        log.info("이벤트 고아 파일 정리 종료 - 총 {}건 삭제", deletedCount);
/*
        고아 파일 삭제 완료: GuestbookController.java
        고아 파일 삭제 완료: guestbooklist.html
        고아 파일 삭제 완료: GuestbookService.java
        고아 파일 삭제 완료: GuestbookService_imple.java
        고아 파일 삭제 완료: guestbookview.html
        고아 파일 삭제 완료: sist_logo.png
        이벤트 고아 파일 정리 종료 - 총 6건 삭제
*/
    }
    

    
}
