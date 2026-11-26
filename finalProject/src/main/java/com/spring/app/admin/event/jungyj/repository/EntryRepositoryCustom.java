package com.spring.app.admin.event.jungyj.repository;

import java.util.List;

import com.spring.app.entity.jungyj.Entry;

public interface EntryRepositoryCustom {

	List<Entry> findEntriesByEventseq(Integer eventseq);
    
	// 이벤트에 참여한 모든 인원 조회
	List<Entry> findByEventEntryUser(Integer eventseq);

}
