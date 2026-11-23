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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "tbl_user", 
    uniqueConstraints = {
        @UniqueConstraint(name = "UQ_TBL_USER_SOCIAL_userid", columnNames = "social_userid"),
        @UniqueConstraint(name = "UQ_TBL_USER_USERID", columnNames = "userid"),
        @UniqueConstraint(name = "UQ_TBL_USER_EMAIL", columnNames = "email")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(
            name = "user_seq_gen",
            sequenceName = "seq_userseq", // 실제 Oracle에 생성한 시퀀스명으로 변경하세요
            allocationSize = 1
    )
    @Column(name = "userseq")
    private Long userseq;

    @Column(name = "social_userid", length = 50)
    private String socialUserid;

    @Column(name = "userid", nullable = false, length = 50)
    private String userid;

    @Column(name = "passwd", nullable = false, length = 200)
    private String passwd;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "email", nullable = false, length = 200)
    private String email;

    @Column(name = "mobile", nullable = false, length = 200)
    private String mobile;

    @ColumnDefault("0")
    @Column(name = "email_checked", nullable = false)
    private Integer emailChecked; // 1: 인증완료, 0: 미인증

    @Column(name = "summary", length = 200)
    private String summary;

    @Column(name = "profile_orgfilename", length = 255)
    private String profileOrgfilename;

    @Column(name = "profile_sysfilename", nullable = false, length = 255)
    private String profileSysfilename;

    // JPA save() 호출 시 자바 단에서도 기본값 보장
    @PrePersist
    public void prePersist() {
        if (this.emailChecked == null) {
            this.emailChecked = 0;
        }
        // 회원가입 시 프로필 사진을 안 올렸을 경우 NOT NULL 제약조건 방어용 기본 이미지 세팅
        if (this.profileSysfilename == null || this.profileSysfilename.isBlank()) {
            this.profileSysfilename = "default_profile.png";
        }
    }
}
