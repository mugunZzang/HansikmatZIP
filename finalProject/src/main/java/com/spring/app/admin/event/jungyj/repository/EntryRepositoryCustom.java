package com.spring.app.admin.event.jungyj.repository;

import java.util.List;

import com.spring.app.entity.jungyj.Entry;

public interface EntryRepositoryCustom {

	List<Entry> findEntriesByEventseq(Integer eventseq);
    
}
