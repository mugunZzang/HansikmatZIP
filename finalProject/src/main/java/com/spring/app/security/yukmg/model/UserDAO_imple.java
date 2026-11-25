package com.spring.app.security.yukmg.model;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.spring.app.security.yukmg.domain.UserDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDAO_imple implements UserDAO {

	@Qualifier("sqlsession")
	private final SqlSessionTemplate sqlsession;
	
	@Override
	public List<UserDTO> getChefRanking() {
				
		List<UserDTO> UserDTOList = sqlsession.selectList("chef.getChefRanking");
	      
		List<UserDTO> list = null;
	      
	      if(UserDTOList != null) {
	         
	         list = new ArrayList<>();
	         
	         for(UserDTO userDTO : UserDTOList) {
	            
	            
	            String userid = userDTO.getUserid();
	            
	            //List<String> authorityList = sqlsession.selectList("chef.authorityList", userid); 
	            
	            list.add(userDTO);
	         }// end of for-----------------
	         
	      }
	      
	      return list;
	}

}
