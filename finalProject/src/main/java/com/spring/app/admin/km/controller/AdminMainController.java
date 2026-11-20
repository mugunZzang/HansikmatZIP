package com.spring.app.admin.km.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/km/admin")
public class AdminMainController {

	
	@GetMapping("admin_index")    // http://localhost:8000/hansikmatzip/admin_index
    public String index() {
    	
    	return "km/admin/admin_index";
    	// src/main/resources/templates/km/admin/admin_index.html 파일을 만들어야 한다.
    }
}
