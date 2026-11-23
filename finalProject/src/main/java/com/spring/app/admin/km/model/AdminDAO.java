package com.spring.app.admin.km.model;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDAO {

	// 오늘자 멤버쉽수
	int todaymembership();

	// 오늘자 신규 레시피 수
	int todaywriterecipe();

	// 오늘자 신규 쉐프 수
	int todaynewchef();
	
}
