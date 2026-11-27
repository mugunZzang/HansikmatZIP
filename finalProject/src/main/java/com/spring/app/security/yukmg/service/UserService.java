package com.spring.app.security.yukmg.service;

import java.util.List;
import java.util.Map;

import com.spring.app.security.yukmg.domain.UserDTO;

public interface UserService {

	// 총 셰프 수 조회 (페이징 계산용)
    int getTotalChefCount();
	
    // 셰프 랭킹 페이징 목록 조회
	List<UserDTO> getChefRanking(Map<String, String> paraMap);

}
