package com.spring.app.recipe.km.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Ingredient_bundleDTO {
	private Integer ingredient_bundleseq;               
	private Integer fk_recipeseq;  
	private String ingredient_bundle_name;
	private Integer ingredient_bundle_order;
}
