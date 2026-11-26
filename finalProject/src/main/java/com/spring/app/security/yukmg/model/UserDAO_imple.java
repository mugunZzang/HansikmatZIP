package com.spring.app.security.yukmg.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.spring.app.common.AES256;
import com.spring.app.security.yukmg.domain.UserDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDAO_imple implements UserDAO {

	@Qualifier("sqlsession")
	private final SqlSessionTemplate sqlsession;
	
	private final AES256 aes256;
	
	// 쉐프 랭킹
	@Override
	public List<UserDTO> getChefRanking() {
				
		List<UserDTO> userDTOList = sqlsession.selectList("chef.getChefRanking");
	      
		List<UserDTO> list = null;
	      
      if(userDTOList != null) {
         
         list = new ArrayList<>();
         
         for(UserDTO userDTO : userDTOList) {
              
              list.add(userDTO);
         }// end of for-----------------
         
      }
      
      return list;
	}

}
