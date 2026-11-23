package com.spring.app.admin.event.jungyj.service;

import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.spring.app.admin.event.jungyj.domain.EventDTO;
import com.spring.app.admin.event.jungyj.domain.EventEntryDTO;

public interface EventService {

	void eventRegister(EventDTO eventDto, MultipartFile multipartFile) throws Exception;

	// 이벤트 List 조회
	List<EventDTO> list(Map<String, String> paraMap);

	@Nullable
	int getOngoingCount();

	@Nullable
	int getEndedCount();

	// 이벤트 1건 조회
	EventDTO detail(int eventseq);

	// 이벤트 수정하기
	void eventEdit(EventDTO eventDto, MultipartFile multipartFile) throws Exception;

	List<EventEntryDTO> getEntryList(int eventseq);

	// 당첨 처리하기
	void winEntries(int eventseq, List<Integer> entryseqs);


}
