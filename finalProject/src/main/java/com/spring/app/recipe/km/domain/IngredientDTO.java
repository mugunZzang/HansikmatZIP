package com.spring.app.recipe.km.domain;

import lombok.Data;

@Data
public class IngredientDTO {
	private Integer ingredientseq;                 
	private Integer fk_ingredient_bundleseq;  
	private String ingredient_name; 
	private String unit; 
	private Integer quantity;
	private String calorie; 
	private Integer ingredient_order; 
	private String ing_note;
}
