package com.spring.app.entity.jungyj;

import java.time.LocalDate;

import com.spring.app.admin.event.jungyj.domain.EventDTO;

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
@Table(name="tbl_event")
@Data                 // lombok 에서 사용하는 @Data 어노테이션은 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 합쳐놓은 종합선물세트인 것이다.
@AllArgsConstructor   // 모든 필드 값을 파라미터로 받는 생성자를 만들어주는 것
@NoArgsConstructor    // 파라미터가 없는 기본생성자를 만들어주는 것
@Builder
public class Event {

	@Id
	@Column(name="eventseq", columnDefinition="NUMBER")  // columnDefinition 은 DB 컬럼의 정보를 직접 주는 것이다. 예를 들어 columnDefinition = "Nvarchar2(20) default '사원'" 인 것이다. 
	@SequenceGenerator(name="SEQ_EVENT_GENERATOR", sequenceName="seq_eventseq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_EVENT_GENERATOR")
	private Integer eventseq;
	
	
	@Column(name="event_subject", columnDefinition="NVARCHAR2(100)", nullable = false)
	private String eventSubject;
	
	@Column(name="event_startdate", columnDefinition="DATE", nullable=false)
	private LocalDate eventStartdate;
	
	@Column(name="event_enddate", columnDefinition="DATE", nullable=false)
	private LocalDate eventEnddate;
	
	@Column(name="event_drawingdate", columnDefinition="DATE", nullable=false)
	private LocalDate eventDrawingdate;
	
	@Column(name="event_image_org_filename", columnDefinition="VARCHAR2(255)", nullable=false)
	private String eventImageOrgFilename;

	@Column(name="event_image_filename", columnDefinition="VARCHAR2(255)", nullable=false)
	private String eventImageFilename;

	@Column(name="event_contents", columnDefinition="NVARCHAR2(2000)", nullable = false)
	private String eventContents;
	
	@Column(name="event_total_winners", columnDefinition="Number", nullable = false)
	private int eventTotalWinners;
	
	@PrePersist
	public void prePersist() {
	    if (this.eventEnddate != null && this.eventDrawingdate == null) {
	        this.eventDrawingdate = this.eventEnddate.plusDays(2);
	    }
	}

	
	public EventDTO toDTO() {
	    return EventDTO.builder()
	            .eventseq(this.eventseq)
	            .event_subject(this.eventSubject)
	            .event_startdate(this.eventStartdate)
	            .event_enddate(this.eventEnddate)
	            .event_drawingdate(this.eventDrawingdate)
	            .event_image_org_filename(this.eventImageOrgFilename)
	            .event_image_filename(this.eventImageFilename)
	            .event_contents(this.eventContents)
	            .event_total_winners(this.eventTotalWinners)
	            .build();
	}
}
