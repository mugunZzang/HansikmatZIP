package com.spring.app.recipe.km.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.app.recipe.km.domain.RecipeDTO;
import com.spring.app.recipe.km.service.RecipeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/km/recipe/")
public class RecipeController {

	private final RecipeService service;
	
	// @PreAuthorize("isAuthenticated()")      // 로그인한 사람만 접근할 수 있음
	@GetMapping("recipeRegister")   
    public String recipeRegister() {
    		
		return "km/recipe/recipeRegister";
    }// end of public String recipeRegister() {
	
	// @PreAuthorize("isAuthenticated()")      // 로그인한 사람만 접근할 수 있음
	@PostMapping("recipeRegister")  
	@ResponseBody
    public Map<String,Integer> recipeRegister(RecipeDTO recipeDto) {
		
		int n = service.recipeRegister();
    	
    	Map<String, Integer> map = new HashMap<>();
    	map.put("num", n);
    	
		return map;
    }// end of public String recipeRegister() {
	
}
