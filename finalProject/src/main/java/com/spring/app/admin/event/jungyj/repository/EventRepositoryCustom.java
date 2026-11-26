package com.spring.app.admin.event.jungyj.repository;

import java.time.LocalDate;
import java.util.List;

import com.spring.app.entity.jungyj.Entry;
import com.spring.app.entity.jungyj.Event;

public interface EventRepositoryCustom {

	List<Event> findDrawingEvents(LocalDate today);

    // 서버에 저장되어있는 파일명 조회(스케줄링을 위한 것)
    List<String> findAllUsedImageFilenames();

}
