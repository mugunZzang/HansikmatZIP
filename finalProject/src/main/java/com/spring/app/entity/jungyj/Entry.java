package com.spring.app.entity.jungyj;

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
@Table(name="tbl_entry")
@Data                 // lombok 에서 사용하는 @Data 어노테이션은 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 합쳐놓은 종합선물세트인 것이다.
@AllArgsConstructor   // 모든 필드 값을 파라미터로 받는 생성자를 만들어주는 것
@NoArgsConstructor    // 파라미터가 없는 기본생성자를 만들어주는 것
@Builder
public class Entry {
	
	@Id
    @Column(name = "entryseq", columnDefinition = "NUMBER")
    @SequenceGenerator(name = "SEQ_ENTRY_GENERATOR", sequenceName = "seq_entryseq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ENTRY_GENERATOR")
    private Integer entryseq;

    @Column(name = "fk_eventseq", columnDefinition = "NUMBER", nullable = false)
    private Integer fkEventseq;

    @Column(name = "fk_userseq", columnDefinition = "NUMBER", nullable = false)
    private Integer fkUserseq;

    @Column(name = "fk_recipeseq", columnDefinition = "NUMBER", nullable = false)
    private Integer fkRecipeseq;

    @Column(name = "chk_win", columnDefinition = "NUMBER(1) default 0", nullable = false)
    private int chkWin;

}
