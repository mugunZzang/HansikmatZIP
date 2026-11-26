package com.spring.app.admin.event.jungyj.repository;


import static com.spring.app.entity.jungyj.QEvent.event;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.app.entity.jungyj.Entry;
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
	                event.eventEnddate.loe(today)       // 종료일 <= 오늘
	                .and(event.eventDrawingdate.gt(today)) // 발표일 > 오늘 (즉, 오늘 < 발표일)
	            )
	            .orderBy(event.eventseq.desc())
	            .fetch();
	}
	
	
    // 서버에 저장되어있는 파일명 조회(스케줄링을 위한 것)
	@Override
	public List<String> findAllUsedImageFilenames() {
		return jPAQueryFactory
	            .select(event.eventImageFilename)
	            .distinct()
	            .from(event)
	            .where(event.eventImageFilename.isNotNull())
	            .fetch();
	}




}
