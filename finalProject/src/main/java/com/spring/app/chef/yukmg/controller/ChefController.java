package com.spring.app.chef.yukmg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // @RequiredArgsConstructor 는 Lombok 라이브러리에서 제공하는 애너테이션으로, final 필드 또는 @NonNull이 붙은 필드에 대해 이 필드만 포함하는 생성자를 자동으로 생성해준다.
@RequestMapping(value = "/chef/")
public class ChefController {

	@GetMapping("chef_list")
	public String list(Model model) {
		
		
		return "chef/yukmg/chef_list";
	    //  src/main/resources/templates/product/register.html 페이지를 만들어야 한다.		
	}
	
}
