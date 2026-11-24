package com.spring.app.entity.jungyj;

import java.time.LocalDateTime;

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
@Table(name="tbl_qna")
@Data                 // lombok 에서 사용하는 @Data 어노테이션은 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 합쳐놓은 종합선물세트인 것이다.
@AllArgsConstructor   // 모든 필드 값을 파라미터로 받는 생성자를 만들어주는 것
@NoArgsConstructor    // 파라미터가 없는 기본생성자를 만들어주는 것
@Builder
public class Qna {

	@Id
    @Column(name = "qnaseq", columnDefinition = "NUMBER")
    @SequenceGenerator(name = "SEQ_QNA_GENERATOR", sequenceName = "seq_qnaseq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_QNA_GENERATOR")
    private Integer qnaseq;

    @Column(name = "fk_userseq", columnDefinition = "NUMBER", nullable = false)
    private Integer fkUserseq;

    @Column(name = "qna_subject", columnDefinition = "NVARCHAR2(50)", nullable = false)
    private String qnaSubject;

    @Column(name = "qna_content", columnDefinition = "NVARCHAR2(500)", nullable = false)
    private String qnaContent;

    @Column(name = "qna_status", columnDefinition = "NVARCHAR2(10)   default '접수'", nullable = false)
    private String qnaStatus;

    @Column(name = "answer", columnDefinition = "NVARCHAR2(500)")
    private String answer;

    @Column(name = "answer_date", columnDefinition = "DATE")
    private LocalDateTime answerDate;
    
    @Column(name = "question_date", columnDefinition = "DATE  default sysdate ", nullable = false)
    private LocalDateTime questionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_userseq", insertable = false, updatable = false)
    private AppUser qnaUser;
}
