package com.spring.app.recipe.km.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String recipeRegister(RecipeDTO recipeDto) {
    		
		return "km/recipe/recipeRegister";
    }// end of public String recipeRegister() {
	
}
