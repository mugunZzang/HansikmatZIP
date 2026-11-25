package com.spring.app.event.kimkc.service;

import static com.spring.app.kimkc.entity.QEvent.event;

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
import com.spring.app.kimkc.entity.Event;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventListService_imple implements EventListService {

	
	private final JPAQueryFactory jPAQueryFactory;

	
	
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
}
