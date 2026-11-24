package com.spring.app.event.kimkc.service;

import org.springframework.data.domain.Page;

import com.spring.app.kimkc.entity.Event;

public interface EventListService {

	// 기본 페이지인 [진행중인 이벤트] 탭의 1페이지 이벤트 리스트 갖고오기
	Page<Event> getPageEvent(int currentShowPageNo, int sizePerPage, String status);

}
