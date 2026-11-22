package com.spring.app.admin.event.jungyj.repository;

import static com.spring.app.entity.jungyj.QEntry.entry;
import static com.spring.app.entity.jungyj.QEventRecipe.eventRecipe;
import static com.spring.app.entity.jungyj.QEventUser.eventUser;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.app.admin.event.jungyj.domain.EventEntryDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryRepositoryImpl implements EntryRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<EventEntryDTO> findEntriesByEventseq(Integer fkEventseq) {
	    return queryFactory
	            .select(Projections.constructor(EventEntryDTO.class,
	                    entry.entryseq,
	                    entry.fkEventseq,
	                    entry.fkUserseq,
	                    entry.fkRecipeseq,
	                    entry.chkWin,
	                    eventUser.nickname,
	                    eventRecipe.rSubject,
	                    eventRecipe.rImgname
	            ))
	            .from(entry)
	            .join(eventUser).on(entry.fkUserseq.eq(eventUser.userseq))
	            .join(eventRecipe).on(entry.fkRecipeseq.eq(eventRecipe.recipeseq))
	            .where(entry.fkEventseq.eq(fkEventseq))
	            .orderBy(entry.entryseq.asc())
	            .fetch();
	}

}
