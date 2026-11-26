package com.spring.app.springscheduler.jungyj.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spring.app.admin.event.jungyj.domain.EventEntryDTO;
import com.spring.app.admin.event.jungyj.repository.EventRepository;
import com.spring.app.admin.event.jungyj.service.EventService;
import com.spring.app.admin.notice.jungyj.repository.NoticeRepository;
import com.spring.app.entity.jungyj.Event;
import com.spring.app.entity.jungyj.SendNotice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventDrawingDateNoticeScheduler {

	private final EventRepository eventRepository;
	private final NoticeRepository noticeRepository;

	private final EventService eventService;
	
	// 매일 자정 5분 실행 (초 분 시 일 월 요일)
	// 발표일에 맞춰 당첨/미당첨자에게 알림 보내기
    //@Scheduled(cron = "0 * * * * *")
	@Scheduled(cron = "0 5 0 * * *")
    public void sendNotice() {
    	// 오늘 일자
    	LocalDate toDay = LocalDate.now(); 
    	
    	// 발표일 == 오늘 날짜
    	List<Event> drawingDateTodayList = eventRepository.findByEventDrawingdateEquals(toDay);
    	
    	if (drawingDateTodayList == null || drawingDateTodayList.isEmpty()) {
    		log.info("{}에는 발표예정인 이벤트가 없습니다.", toDay);
    		// 2026-11-26에는 발표예정인 이벤트가 없습니다.
            return;
        } 
    	
    	List<SendNotice> noticeList = new ArrayList<>();
    	
        for (Event event : drawingDateTodayList) {
            Integer eventSeq = event.getEventseq();
            String eventTitle = event.getEventSubject(); // 이벤트명이 있다면 활용

            // 알림을 보낼 객체 저장해둘 List
            List<EventEntryDTO> allEntries = eventService.getEntryList(eventSeq);

            for (EventEntryDTO entry : allEntries) {
                String contents;
                
                if (entry.getChk_win() == 1) {
                    contents = "[" + eventTitle + "] 이벤트에 당첨되셨습니다! 축하합니다.";
                } else {
                    contents = "[" + eventTitle + "] 이벤트에 참여해주셔서 감사합니다. 아쉽게도 미당첨되었습니다.";
                }

                
                SendNotice notice = SendNotice.createEventNotice(
                        entry.getFk_userseq(),
                        eventSeq,
                        entry.getFk_recipeseq(),
                        contents
                );
                noticeList.add(notice);
            }
        }
        
        log.info("알림 발송 예정 건수: {}건", noticeList.size());
        
        // List가 비어있지 않고 존재한다면 알림 데이터 Insert
        if (!noticeList.isEmpty()) {
        	
        	try {
	            noticeRepository.saveAll(noticeList);
	            log.info("알림 발송 성공 (총 {}건)", noticeList.size());
	            // 알림 발송 성공 (총 5건)
        	} catch(Exception e) {
        		log.error("알림 발송 실패 - 에러 원인: ", e);
        	}
        	
        }
        
    } // end of public void sendNotice()-----------------------
    
    
    	
} 

