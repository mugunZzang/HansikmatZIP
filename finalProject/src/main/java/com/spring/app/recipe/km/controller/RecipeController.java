package com.spring.app.recipe.km.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.app.recipe.km.service.RecipeService;

import jakarta.servlet.http.HttpServletRequest;
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
	
}
