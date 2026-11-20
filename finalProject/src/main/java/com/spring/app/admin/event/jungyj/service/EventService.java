package com.spring.app.admin.event.jungyj.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.spring.app.admin.event.jungyj.domain.EventDTO;

public interface EventService {

	void eventRegister(EventDTO eventDto, MultipartFile multipartFile) throws Exception;

	List<EventDTO> list(Map<String, String> paraMap);

}
