package com.spring.app.admin.km.service;

import java.util.List;
import java.util.Map;

public interface AdminService {

	// 오늘자 멤버쉽수
	int todaymembership();

	// 오늘자 신규 레시피 수
	int todaywriterecipe();

	// 오늘자 신규 쉐프 수
	int todaynewchef();

	// 오늘자 문의사항 
	List<Map<String, String>> todayqna();

	// 마감임박 이벤트
	List<Map<String, String>> endevent();


}
