package com.spring.app.admin.km.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.app.admin.km.service.AdminService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/km/admin")
public class AdminMainController {

	private final AdminService service;
	
	// @PreAuthorize("isAuthenticated()")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("admin_index")   
    public String index() {
    	
    	
		return "km/admin/admin_index";
    }// end of public String index() {}
	
	
	// @PreAuthorize("isAuthenticated()")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("todaymembership") 
	@ResponseBody
    public Map<String,Integer> todaymembership() {
		
		int n = service.todaymembership();
    	
    	Map<String, Integer> map = new HashMap<>();
    	map.put("count", n);
    	
    	return map;
    }// end of public Map<String,Integer> todaymembership() {
	
	// @PreAuthorize("isAuthenticated()")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("todaywriterecipe") 
	@ResponseBody
    public Map<String,Integer> todaywriterecipe() {
		
		int n = service.todaywriterecipe();
    	
    	Map<String, Integer> map = new HashMap<>();
    	map.put("count", n);
    	
    	return map;
    }// end of public Map<String,Integer> todaymembership() {
	
	// @PreAuthorize("isAuthenticated()")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("todaynewchef") 
	@ResponseBody
    public Map<String,Integer> todaynewchef() {
		
		int n = service.todaynewchef();
    	
    	Map<String, Integer> map = new HashMap<>();
    	map.put("count", n);
    	
    	return map;
    }// end of public Map<String,Integer> todaymembership() {
	
	// @PreAuthorize("isAuthenticated()")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("todayqna") 
	@ResponseBody
    public List<Map<String,String>> todayqna() {
		
		List<Map<String,String>> mapList = service.todayqna();	
    	
    	return mapList;
    }// end of public List<Map<String,String>> todayqna() {

	// @PreAuthorize("isAuthenticated()")         // 로그인한 사람만 접근할 수 있음
	@GetMapping("endevent") 
	@ResponseBody
    public List<Map<String,String>> endevent() {
		
		List<Map<String,String>> mapList = service.endevent();	
    	
    	return mapList;
    }// end of public List<Map<String,String>> endevent() {
	
}
