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
@Table(name="tbl_notice")
@Data                 // lombok 에서 사용하는 @Data 어노테이션은 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 합쳐놓은 종합선물세트인 것이다.
@AllArgsConstructor   // 모든 필드 값을 파라미터로 받는 생성자를 만들어주는 것
@NoArgsConstructor    // 파라미터가 없는 기본생성자를 만들어주는 것
@Builder
public class SendNotice {
	
	@Id
    @Column(name = "noticeseq", columnDefinition = "NUMBER")
    @SequenceGenerator(name = "SEQ_NOTICE_GENERATOR", sequenceName = "seq_noticeseq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NOTICE_GENERATOR")
    private Integer noticeSeq;

    @Column(name = "fk_userseq", columnDefinition = "NUMBER", nullable = false)
    private Integer fkUserseq;

    @Column(name = "fk_followerseq", columnDefinition = "NUMBER")
    private Integer fkFollowerseq;

    @Column(name = "fk_eventseq", columnDefinition = "NUMBER")
    private Integer fkEventseq;
    
    @Column(name = "fk_recipeseq", columnDefinition = "NUMBER")
    private Integer fkRecipeseq;
    
    @Column(name = "notice_contents", columnDefinition = "NVARCHAR2(500)", nullable = false)
    private String noticeContents;
    
    @Column(name = "view_status", columnDefinition = "NUMBER(1)", nullable = false)
    private Integer viewStatus;
    

    public static SendNotice createEventNotice(Integer userSeq, Integer eventSeq, Integer recipeSeq, String contents) {
        return SendNotice.builder()
                .fkUserseq(userSeq)
                .fkEventseq(eventSeq)
                .fkRecipeseq(recipeSeq)
                .noticeContents(contents)
                .viewStatus(0) // 열람여부 1 고정
                .build();
        // noticeSeq -> 시퀀스 자동 생성
        // fkFollowerseq -> 미지정 시 null
    }
}
