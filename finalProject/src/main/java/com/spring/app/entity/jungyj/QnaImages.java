package com.spring.app.entity.jungyj;

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
@Table(name="tbl_qna_images")
@Data                 // lombok 에서 사용하는 @Data 어노테이션은 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 합쳐놓은 종합선물세트인 것이다.
@AllArgsConstructor   // 모든 필드 값을 파라미터로 받는 생성자를 만들어주는 것
@NoArgsConstructor    // 파라미터가 없는 기본생성자를 만들어주는 것
@Builder
public class QnaImages {

	@Id
    @Column(name = "qna_imgseq", columnDefinition = "NUMBER")
    @SequenceGenerator(name = "SEQ_QNAIMAGE_GENERATOR", sequenceName = "seq_qna_imgseq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_QNAIMAGE_GENERATOR")
    private Integer qnaImgseq;

    @Column(name = "fk_qnaseq", columnDefinition = "NUMBER", nullable = false)
    private Integer fkQnaseq;

    @Column(name = "qna_orgfilename", columnDefinition = "VARCHAR2(255)", nullable = false)
    private String qnaOrgfilename;

    @Column(name = "qna_sysfilename", columnDefinition = "VARCHAR2(255)", nullable = false)
    private String qnaSysfilename;

    // 연관관계 정의
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_qnaseq", insertable = false, updatable = false)
    private Qna qna;
	
}
