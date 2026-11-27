package com.spring.app.security.yukmg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.app.common.AES256;
import com.spring.app.security.yukmg.domain.UserDTO;
import com.spring.app.security.yukmg.model.UserDAO;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class UserService_imple implements UserService {

	private final UserDAO userdao;
	
	private final AES256 aes256;
	
	// 쉐프 조회
	@Override
	public List<UserDTO> getChefRanking(String sortType) {
		List<UserDTO> userDTOList = userdao.getChefRanking(sortType);		
		return userDTOList;
	}

}
