package com.spring.app.entity.jungyj;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_recipe")
@Getter
@NoArgsConstructor
public class EventRecipe {

	@Id
    @Column(name = "recipeseq")
    private Integer recipeseq;

    @Column(name = "r_subject")
    private String rSubject;

    @Column(name = "r_imgname")
    private String rImgname;
    

    
}
