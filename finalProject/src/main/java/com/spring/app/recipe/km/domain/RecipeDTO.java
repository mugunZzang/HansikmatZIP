package com.spring.app.recipe.km.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RecipeDTO {

	private Integer recipeseq;             
	private Integer fk_userseq;  
	private String r_subject;
	private String r_orgimgname;
	private String r_imgname;
	private String r_intro;
	private String r_url;
	private String r_time;
	private String r_level;
	private String people;
	private String paid_check; 
	private String tip_caution; 
	private Integer r_views; 
	private String ai_review; 
	private Integer ai_totalcalorie;
	private LocalDate r_writedate;
	private String situation_ca;
	private String type_ca; 
	private String main_ca;
	private String recipe_ca; 
	private String view_check;
	
}
