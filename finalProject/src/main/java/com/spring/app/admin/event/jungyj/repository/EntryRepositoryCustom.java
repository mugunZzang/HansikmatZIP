package com.spring.app.admin.event.jungyj.repository;

import java.util.List;

import com.spring.app.admin.event.jungyj.domain.EventEntryDTO;

public interface EntryRepositoryCustom {

	List<EventEntryDTO> findEntriesByEventseq(Integer eventseq);
    
}
