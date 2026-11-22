package com.spring.app.admin.event.jungyj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.app.entity.jungyj.Entry;

public interface EntryRepository extends JpaRepository<Entry, Integer> {

	long countByFkEventseq(Integer eventseq);
	
}
