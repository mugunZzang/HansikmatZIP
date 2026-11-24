package com.spring.app.entity.jungyj;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_user")
@Getter
@NoArgsConstructor
public class AppUser {

	@Id
	@Column(name = "userseq")
    private Integer userseq;

    @Column(name = "nickname")
    private String nickname;
    

}
