package com.spring.app.admin.event.jungyj.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.app.entity.jungyj.Event;

public interface EventRepository extends JpaRepository<Event, Integer>, EventRepositoryCustom {
	
/*
	 LessThan            findByAgeLessThan          … where x.age < ?1

	 LessThanEqual       findByAgeLessThanEqual     … where x.age <= ?1

     GreaterThan         findByAgeGreaterThan       … where x.age > ?1

     GreaterThanEqual    findByAgeGreaterThanEqual  … where x.age >= ?1
     
     Spring Boot 공식 문서 JPA 쿼리 메서드 참조 URL
     https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
*/
	
	 
	// 1. 진행 중인 이벤트 목록 (종료일 >= 오늘, 최신순)
    List<Event> findByEventEnddateGreaterThanEqualOrderByEventseqDesc(LocalDate today);

    // 2. 종료된 이벤트 목록 (종료일 < 오늘, 최신순)
    List<Event> findByEventEnddateLessThanOrderByEventseqDesc(LocalDate today);
    
	// 진행 중인 이벤트 개수 (응모 중 + 당첨 선정 대기 = 발표일이 오늘보다 미래인 것)
	int countByEventDrawingdateGreaterThan(LocalDate now);

	// 종료된 이벤트 개수 (발표일이 오늘이거나 이미 지난 것)
	int countByEventDrawingdateLessThanEqual(LocalDate now);


}
