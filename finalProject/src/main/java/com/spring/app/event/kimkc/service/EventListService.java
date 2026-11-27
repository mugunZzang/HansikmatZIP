package com.spring.app.event.kimkc.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.app.event.kimkc.domain.EntryDTO;
import com.spring.app.event.kimkc.domain.EventListDTO;
import com.spring.app.kimkc.entity.Entry;
import com.spring.app.kimkc.entity.Event;

public interface EventListService {

	// 기본 페이지인 [진행중인 이벤트] 탭의 1페이지 이벤트 리스트 갖고오기
	Page<Event> getPageEvent(int currentShowPageNo, int sizePerPage, String status);

	// 이벤트 상세정보 가져오기
	EventListDTO getEventDetail(int eventseq);

	// 이벤트 참여 레시피 리스트 가져오기
	List<EntryDTO> getEntryList(int eventseq);

	// 페이지별 이벤트 참여 레시피 목록 갖고오기
	Page<Entry> getPageEntry(int pageNo, int sizePerPage, int eventseq);

}
