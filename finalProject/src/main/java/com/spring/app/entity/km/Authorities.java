package com.spring.app.entity.km;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tbl_authorities")
@Data                 // lombok 에서 사용하는 @Data 어노테이션은 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 합쳐놓은 종합선물세트인 것이다.
@AllArgsConstructor   // 모든 필드 값을 파라미터로 받는 생성자를 만들어주는 것
@NoArgsConstructor    // 파라미터가 없는 기본생성자를 만들어주는 것
@Builder
public class Authorities {

	@Id
	@Column(name="authseq", columnDefinition="NUMBER")  // columnDefinition 은 DB 컬럼의 정보를 직접 주는 것이다. 예를 들어 columnDefinition = "Nvarchar2(20) default '사원'" 인 것이다. 
	@SequenceGenerator(name="SEQ_BOARD_GENERATOR", sequenceName="seq_authseq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_BOARD_GENERATOR")
	private Long authseq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userseq", nullable = false, updatable=false)  // 이 필드는 UPDATE 를 할 수 없도록 제외시킴. 즉, 한번 데이터 입력 후 memberid 컬럼의 값은 수정 불가라는 뜻이다.   
	private User user;
	
	@Column(name = "authority")
	private String authority;
	

	
	
	

		
	
}
