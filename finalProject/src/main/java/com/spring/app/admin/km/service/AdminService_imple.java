package com.spring.app.admin.km.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.spring.app.admin.km.model.AdminDAO;

import lombok.RequiredArgsConstructor;

//==== Service 선언 ==== //
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단

@Service
@RequiredArgsConstructor
public class AdminService_imple implements AdminService {

	private final AdminDAO adminDao;

	// 오늘자 멤버쉽수
	@Override
	public int todaymembership() {
		
		return adminDao.todaymembership();
	}

	// 오늘자 신규 레시피 수
	@Override
	public int todaywriterecipe() {
		
		return adminDao.todaywriterecipe();
	}

	// 오늘자 신규 쉐프 수
	@Override
	public int todaynewchef() {
		
		return adminDao.todaynewchef();
	}

	// 문의사항 불러오기 5개만
	@Override
	public List<Map<String, Object>> todayqna() {
		
		return adminDao.todayqna();
	}
	
	// 마감임박 이벤트
	@Override
	public List<Map<String, Object>> endevent() {
		
		return adminDao.endevent();
	}

	// 검수 할 레시피 보여주기
	@Override
	public List<Map<String, Object>> revealrecipe(String currentShowPageNo) {
		
		return adminDao.revealrecipe(currentShowPageNo);
	}

	// 총 검수 할 레시피 건수 (totalCount)
	@Override
	public int getTotalCount() {
		
		return adminDao.getTotalCount();
	}
	
	
}
