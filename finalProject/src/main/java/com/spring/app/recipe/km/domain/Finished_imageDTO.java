package com.spring.app.recipe.km.domain;

import lombok.Data;

@Data
public class Finished_imageDTO {
	private Integer finished_imageseq;               
	private Integer fk_recipeseq; 
	private String rcp_original_file_name;
	private String rcp_stored_file_name; 
}
