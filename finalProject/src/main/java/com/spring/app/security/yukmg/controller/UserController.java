package com.spring.app.security.yukmg.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.app.security.yukmg.domain.UserDTO;
import com.spring.app.security.yukmg.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/security/")
public class UserController {

	private final UserService userservice;
	
	// 누구나 접근 가능한 URL
    // 조건이 없으므로, 로그인을 하지 않은 상태이거나 또는 로그인을 성공한 상태 모두 메뉴가 보여지는 것이다 
    @GetMapping(value="everybody")
    public String everybody(){
       
       return "security/yukmg/everybody";
    }
	    
    // 누구나 접근 가능한 URL. 쉐프 - 랭킹
    @GetMapping(value="chef/chef-list")
    public String chefRanking(Model model){
       
       List<UserDTO> UserDTOList = userservice.getChefRanking();
       
       model.addAttribute("UserDTOList", UserDTOList);
       
       return "chef/chef-list";
    }
}
