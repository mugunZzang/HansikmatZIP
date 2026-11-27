package com.spring.app.security.yukmg.service;

import java.util.List;

import com.spring.app.security.yukmg.domain.UserDTO;

public interface UserService {

	// 쉐프 랭킹 조회
	List<UserDTO> getChefRanking(String sortType);

}
