package com.spring.app.entity.km;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tbl_user")
@Data                 // lombok 에서 사용하는 @Data 어노테이션은 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 합쳐놓은 종합선물세트인 것이다.
@AllArgsConstructor   // 모든 필드 값을 파라미터로 받는 생성자를 만들어주는 것
@NoArgsConstructor    // 파라미터가 없는 기본생성자를 만들어주는 것
@Builder
public class User {

	@Id
	@Column(name="userseq", columnDefinition="NUMBER")  // columnDefinition 은 DB 컬럼의 정보를 직접 주는 것이다. 예를 들어 columnDefinition = "Nvarchar2(20) default '사원'" 인 것이다. 
	@SequenceGenerator(name="SEQ_BOARD_GENERATOR", sequenceName="seq_userseq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_BOARD_GENERATOR")
	private Long userseq;
	
	@Column(name = "social_userid")
	private String social_userid;
	
	@Column(name = "userid")
	private String userid;
	
	@Column(name = "passwd")
	private String passwd;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "nickname")
	private String nickname;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile")
	private String mobile;
	
	@Column(name = "email_checked",
			columnDefinition = "NUMBER DEFAULT 0")
	private Integer email_checked;
	
	@Column(name = "summary")
	private String summary;
	
	@Column(name = "profile_orgfilename")
	private String profile_orgfilename;
	
	@Column(name = "profile_sysfilename")
	private String profile_sysfilename;
	
	
	

		
	
}
