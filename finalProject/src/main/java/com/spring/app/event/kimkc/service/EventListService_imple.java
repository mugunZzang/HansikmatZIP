package com.spring.app.event.kimkc.service;

import static com.spring.app.kimkc.entity.QEvent.event;
import static com.spring.app.kimkc.entity.QEntry.entry;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.app.event.kimkc.domain.EntryDTO;
import com.spring.app.event.kimkc.domain.EventListDTO;
import com.spring.app.event.kimkc.model.EventListDAO;
import com.spring.app.kimkc.entity.Entry;
import com.spring.app.kimkc.entity.Event;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventListService_imple implements EventListService {

	
	private final JPAQueryFactory jPAQueryFactory;
	private final EventListDAO dao;
	
	
	// 기본 페이지인 [진행중인 이벤트] 탭의 1페이지 이벤트 리스트 갖고오기
	@Override
	public Page<Event> getPageEvent(int currentShowPageNo, int sizePerPage, String status) {

		
		Page<Event> page = Page.empty();
		
		Pageable pageable = PageRequest.of(currentShowPageNo-1, sizePerPage, Sort.by(Sort.Direction.DESC, "eventseq"));
		
		
		
		// 진행중, 종료 이벤트 구분하기
		LocalDate today = LocalDate.now();
		BooleanExpression condition = Expressions.TRUE;
		if(status.equals("ongoing")) {
			// status 가 ongoing 이면 "진행중"인것만
			condition = condition.and(event.eventEnddate.goe(today));
		}
		else {
			condition = condition.and(event.eventEnddate.lt(today));
		}
		
		
		// 이벤트 리스트 불러오기
		List<Event> eventList = jPAQueryFactory
				.selectFrom(event)
				.where(condition)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(event.eventSeq.desc())
				.fetch();
		
		// 진행 또는 종료 이벤트 수
		Long total = jPAQueryFactory
				.select(event.count())
				.from(event)
				.where(condition)
				.fetchOne();
				
		page = new PageImpl<>(eventList, pageable, total);
		
		
		return page;
	}


	// ========================================================================================================================

	
	// 이벤트 상세 정보 가져오기
	@Override
	public EventListDTO getEventDetail(int eventseq) {
		EventListDTO eventDto = dao.getEventDetail(eventseq);
		return eventDto;
	}

	// 이벤트 참여자 목록 가져오기
	@Override
	public List<EntryDTO> getEntryList(int eventseq) {
		List<EntryDTO> entryList = dao.getEntryList(eventseq);
		return entryList;
	}


	// ========================================================================================================================
	
	
	// 페이지 별로 이벤트 참여 레시피 목록 갖고오기
	@Override
	public Page<Entry> getPageEntry(int pageNo, int sizePerPage, int eventseq) {
		
		Page<Entry> page = Page.empty();
		
		Pageable pageable = PageRequest.of(pageNo-1, sizePerPage, Sort.by(Sort.Direction.DESC, "entryseq"));
		
		BooleanExpression condition = Expressions.TRUE;
		condition = condition.and(entry.event.eventSeq.eq(Long.valueOf(eventseq)));
		
		// 참여 레시피 리스트 불러오기
		List<Entry> entryList = jPAQueryFactory
				.selectFrom(entry)
				.join(entry.user).fetchJoin()
				.join(entry.recipe).fetchJoin()
				.where(condition)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(entry.entrySeq.desc())
				.fetch();
		
		// 총 참여 레시피 수
		Long total = jPAQueryFactory
				.select(entry.count())
				.from(entry)
				.where(condition)
				.fetchOne();

		page = new PageImpl<>(entryList, pageable, total);
		
		return page;
	}
}
