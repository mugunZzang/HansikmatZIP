package com.spring.app.admin.event.jungyj.repository;


import static com.spring.app.entity.jungyj.QEvent.event;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.app.entity.jungyj.Event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

	
	private final JPAQueryFactory jPAQueryFactory;
	
	@Override
	public List<Event> findDrawingEvents(LocalDate today) {
	    return jPAQueryFactory
	            .selectFrom(event)
	            .where(
	                event.eventEnddate.lt(today)
	                .and(event.eventDrawingdate.goe(today))
	            )
	            .orderBy(event.eventseq.desc())
	            .fetch();
	}

}
