package com.spring.app.admin.event.jungyj.repository;


import static com.spring.app.entity.jungyj.QEntry.entry;
import static com.spring.app.entity.jungyj.QEventRecipe.eventRecipe;
import static com.spring.app.entity.jungyj.QAppUser.appUser;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.app.entity.jungyj.Entry;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryRepositoryImpl implements EntryRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<Entry> findEntriesByEventseq(Integer fkEventseq) {
		
		return queryFactory
	            .selectFrom(entry)
	            .join(entry.eventUser, appUser).fetchJoin()
	            .join(entry.eventRecipe, eventRecipe).fetchJoin()
	            .where(entry.event.eventseq.eq(fkEventseq))
	            .orderBy(entry.entryseq.asc())
	            .fetch();
        
	}

}
