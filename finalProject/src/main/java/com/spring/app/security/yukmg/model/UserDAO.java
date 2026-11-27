package com.spring.app.security.yukmg.model;

import java.util.List;

import com.spring.app.security.yukmg.domain.UserDTO;

public interface UserDAO {

	// 쉐프 조회
	List<UserDTO> getChefRanking(String sortType);
	
}
