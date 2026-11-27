package com.spring.app.security.yukmg.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.spring.app.common.AES256;
import com.spring.app.security.yukmg.domain.UserDTO;
import com.spring.app.security.yukmg.model.UserDAO;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class UserService_imple implements UserService {

	private final UserDAO userDAO;
	
	private final AES256 aes256;	

	// 총 셰프 수 조회 (페이징 계산용)
	@Override
	public int getTotalChefCount() {
		int totalCount = userDAO.getTotalChefCount();
		return totalCount;
	}

	// 셰프 랭킹 페이징 목록 조회
	@Override
	public List<UserDTO> getChefRanking(Map<String, String> paraMap) {
		List<UserDTO> userDTOList = userDAO.getChefRanking(paraMap);
		return userDTOList;
	}

}
