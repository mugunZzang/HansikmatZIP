package com.spring.app.event.kimkc.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.spring.app.event.kimkc.domain.EntryDTO;
import com.spring.app.event.kimkc.domain.EventListDTO;

@Mapper
public interface EventListDAO {

	// 이벤트 상세정보 가져오기
	EventListDTO getEventDetail(int eventseq);

	// 이벤트 참여 레시피 리스트 가져오기
	List<EntryDTO> getEntryList(int eventseq);

	//이미 참여한 이벤트인지 검사하기
	int hasUserEnteredEvent(@Param("eventseq") long eventseq, 
							@Param("userseq") long userseq);

	
}
