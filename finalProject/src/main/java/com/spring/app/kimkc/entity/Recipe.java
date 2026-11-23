package com.spring.app.kimkc.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_recipe")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_seq_gen")
    @SequenceGenerator(
            name = "recipe_seq_gen",
            sequenceName = "seq_recipeseq", // 실제 오라클에 생성한 시퀀스명으로 변경하세요
            allocationSize = 1
    )
    @Column(name = "recipeseq")
    private Long recipeSeq;

    // 연관관계 매핑 (@ManyToOne)을 쓰실 경우 User 엔티티로 교체 가능
    @Column(name = "fk_userseq", nullable = false)
    private Long fkUserSeq;

    @Column(name = "r_subject", nullable = false, length = 100)
    private String rSubject;

    @Column(name = "r_orgimgname", nullable = false, length = 255)
    private String rOrgImgName;

    @Column(name = "r_imgname", nullable = false, length = 255)
    private String rImgName;

    @Column(name = "r_intro", nullable = false, length = 1000)
    private String rIntro;

    @Column(name = "r_url", length = 500)
    private String rUrl;

    @Column(name = "r_time", nullable = false, length = 10)
    private String rTime;

    @Column(name = "r_level", nullable = false, length = 10)
    private String rLevel;

    @Column(name = "people", nullable = false, length = 10)
    private String people;

    @Column(name = "paid_check", nullable = false, length = 2)
    private String paidCheck;

    @Column(name = "tip_caution", length = 50)
    private String tipCaution;

    @ColumnDefault("0")
    @Column(name = "r_views", nullable = false)
    private Integer rViews;

    @Column(name = "ai_review", length = 500)
    private String aiReview;

    @Column(name = "ai_totalcalorie")
    private Integer aiTotalCalorie;

    @Column(name = "r_writedate", nullable = false, updatable = false)
    private LocalDateTime rWriteDate;

    @Column(name = "situation_ca", nullable = false, length = 20)
    private String situationCa;

    @Column(name = "type_ca", nullable = false, length = 20)
    private String typeCa;

    @Column(name = "main_ca", nullable = false, length = 20)
    private String mainCa;

    @Column(name = "recipe_ca", nullable = false, length = 20)
    private String recipeCa;

    @Column(name = "view_check", nullable = false, length = 10)
    private String viewCheck;

    // JPA에서 save() 호출 시 DB default 대신 자바 단에서 기본값을 확실히 세팅하는 방법
    @PrePersist
    public void prePersist() {
        if (this.rViews == null) {
            this.rViews = 0;
        }
        if (this.rWriteDate == null) {
            this.rWriteDate = LocalDateTime.now();
        }
    }
}
