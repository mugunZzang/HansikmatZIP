package com.spring.app.admin.event.jungyj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.app.entity.jungyj.Entry;

public interface EntryRepository extends JpaRepository<Entry, Integer>, EntryRepositoryCustom{

	long countByFkEventseq(Integer fkEventseq);

	List<Entry> findByEntryseqInAndFkEventseqAndChkWin(List<Integer> entryseqs, int eventseq, int chkwin);

	long countByFkEventseqAndChkWin(int eventseq, int i);

	
}
