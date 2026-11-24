package com.spring.app.admin.event.jungyj.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.spring.app.admin.event.jungyj.domain.EventDTO;
import com.spring.app.admin.event.jungyj.domain.EventEntryDTO;

public interface EventService {

	// 관리자 이벤트 등록 
	void eventRegister(EventDTO eventDto, MultipartFile multipartFile) throws Exception;

	// 이벤트 List 조회(페이징 처리)	
	Page<EventDTO> getPageEvent(String status, int currentShowPageNo, int sizePerPage);
	
	// 상단 고정용: 당첨 선정 대기 목록 조회 (종료일 < 오늘 <= 발표일)
	List<EventDTO> getDrawingList();
	
	// 진행중인 이벤트 개수
	@Nullable
	int getOngoingCount();

	// 종료된 이벤트 개수
	@Nullable
	int getEndedCount();

	// 이벤트 1건 조회
	EventDTO detail(int eventseq);

	// 이벤트 수정하기
	void eventEdit(EventDTO eventDto, MultipartFile multipartFile) throws Exception;

	// 해당 이벤트에 등록된 레시피 목록 조회
	List<EventEntryDTO> getEntryList(int eventseq);

	// 당첨 처리하기
	void winEntries(int eventseq, List<Integer> entryseqs);
	



}
