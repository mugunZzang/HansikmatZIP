package com.spring.app.admin.event.jungyj.repository;

import java.time.LocalDate;
import java.util.List;

import com.spring.app.entity.jungyj.Event;

public interface EventRepositoryCustom {

	List<Event> findDrawingEvents(LocalDate today);

}
