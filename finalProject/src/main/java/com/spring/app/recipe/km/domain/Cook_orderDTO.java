package com.spring.app.recipe.km.domain;

import lombok.Data;

@Data
public class Cook_orderDTO {
	private Integer cook_orderseq;                
	private Integer fk_recipeseq; 
	private Integer cook_order_step; 
	private String cook_description;
	private String cook_original_filename;
	private String cook_stored_filename;
	private String order_tip;
	private Integer timer;
}
