package com.spring.app.security.yukmg.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.app.security.yukmg.domain.UserDTO;
import com.spring.app.security.yukmg.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/")
public class UserController {

	private final UserService userservice;
	
	// 누구나 접근 가능한 URL
    // 조건이 없으므로, 로그인을 하지 않은 상태이거나 또는 로그인을 성공한 상태 모두 메뉴가 보여지는 것이다 
    @GetMapping(value="everybody")
    public String everybody(){
       
       return "security/yukmg/everybody";
    }
	    
    // 누구나 접근 가능한 URL. 쉐프 - 랭킹
    @GetMapping(value="chef/chef_list")
    public String chefRanking(@RequestParam(name="tab", defaultValue="ranking") String tab,
    							@RequestParam(name="sortType", defaultValue="follower") String sortType,
    							Model model){
       
    	// 1. 랭킹 리스트 조회    	
       List<UserDTO> userDTOList = userservice.getChefRanking(sortType);
       
       // 2. 새로운 쉐프 리스트 조회
       // List<UserDTO> newChefList = userservice.getNewChefList();
       
       model.addAttribute("userDTOList", userDTOList);
       // model.addAttribute("newChefList", newChefList);
       model.addAttribute("tab", tab);           // 현재 탭 상태 ('ranking' 또는 'new')
       model.addAttribute("sortType", sortType); // 버튼 active 유지를 위해 뷰로 전달
       
       return "chef/yukmg/chef_list";
    }
}
