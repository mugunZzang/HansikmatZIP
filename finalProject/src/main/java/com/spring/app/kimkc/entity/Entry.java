package com.spring.app.kimkc.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	name = "tbl_entry",
	uniqueConstraints = {
        @UniqueConstraint(name = "UK_TBL_ENTRY", columnNames = {"fk_userseq", "fk_eventseq"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entry {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ENTRYSEQ_GENERATOR")
    @SequenceGenerator(name = "SEQ_ENTRYSEQ_GENERATOR", sequenceName = "seq_entryseq", allocationSize = 1)
    @Column(name = "entryseq", nullable = false)
    private Long entrySeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_eventseq", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_userseq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_recipeseq", nullable = false)
    private Recipe recipe;

    @Builder.Default
    @Column(name = "chk_win", nullable = false, columnDefinition = "NUMBER(1) DEFAULT 0")
    private Integer chkWin = 0;

    @PrePersist
    public void prePersist() {
        if (this.chkWin == null) {
            this.chkWin = 0;
        }
    }
}
