package com.spring.app.kimkc.entity;

import java.time.LocalDate;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EVENTSEQ_GENERATOR")
    @SequenceGenerator(name = "SEQ_EVENTSEQ_GENERATOR", sequenceName = "seq_eventseq", allocationSize = 1)
    @Column(name = "eventseq", nullable = false)
    private Long eventSeq;

    @Column(name = "event_subject", nullable = false, length = 100)
    private String eventSubject;

    @Column(name = "event_startdate", nullable = false)
    private LocalDate eventStartdate;

    @Column(name = "event_enddate", nullable = false)
    private LocalDate eventEnddate;

    @Column(name = "event_drawingdate", nullable = false)
    private LocalDate eventDrawingdate;

    @Column(name = "event_image_org_filename", nullable = false, length = 255)
    private String eventImageOrgFilename;

    @Column(name = "event_image_filename", nullable = false, length = 255)
    private String eventImageFilename;

    @Column(name = "event_contents", nullable = false, length = 2000)
    private String eventContents;

    @Column(name = "event_total_winners", nullable = false)
    private Integer eventTotalWinners;
}
