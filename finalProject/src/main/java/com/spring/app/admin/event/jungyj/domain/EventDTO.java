package com.spring.app.admin.event.jungyj.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.spring.app.entity.jungyj.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                 // lombok 에서 사용하는 @Data 어노테이션은 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 합쳐놓은 종합선물세트인 것이다.
@AllArgsConstructor   // 모든 필드 값을 파라미터로 받는 생성자를 만들어주는 것
@NoArgsConstructor    // 파라미터가 없는 기본생성자를 만들어주는 것
@Builder              // 생성자 대신, 필요한 값만 선택해서 체이닝 방식으로 객체를 만들 수 있게 해주는 것.
public class EventDTO {

	private Integer eventseq;                        //      number             not null     -- 이벤트 번호                
	private String event_subject;                //      NVARCHAR2(100)     not null     -- 제목  
	private LocalDate event_startdate;           //      DATE               not null     -- 시작일
	private LocalDate event_enddate;             //      DATE               not null     -- 종료일 
	private LocalDate event_drawingdate;         //      DATE               not null     -- 발표일
	private String event_image_org_filename;     //      VARCHAR2(255)      not null     -- 배너이미지 원본 파일명
	private String event_image_filename;         //      VARCHAR2(255)      not null     -- 배너이미지 저장 파일명 
	private String event_contents;               //      NVARCHAR2(2000)    not null     -- 내용
	private int event_total_winners;             //      NUMBER             not null     -- 당첨 정원 
	
	
	// ===== 파일 저장을 위한 필드
	private Long fileSize;       // 파일크기
	
	// ===== 신청 건수 (목록 화면 표시용)
	private long entryCount;

	// Event Entity를 통째로 받아서 DTO로 변환하는 생성자 (목록 조회용)
    public EventDTO(Event event) {
        this.eventseq = event.getEventseq();
        this.event_subject = event.getEventSubject();
        this.event_startdate = event.getEventStartdate();
        this.event_enddate = event.getEventEnddate();
        this.event_drawingdate = event.getEventDrawingdate();
        this.event_image_org_filename = event.getEventImageOrgFilename();
        this.event_image_filename = event.getEventImageFilename();
        this.event_contents = event.getEventContents();
        this.event_total_winners = event.getEventTotalWinners();
    }
	
	// DTO -> Entity 변환 메서드
    public Event toEntity() {
        return Event.builder()
                .eventSubject(this.event_subject)
                .eventStartdate(this.event_startdate)
                .eventEnddate(this.event_enddate)
                .eventDrawingdate(this.event_drawingdate)
                .eventImageOrgFilename(this.event_image_org_filename)
                .eventImageFilename(this.event_image_filename)
                .eventContents(this.event_contents)
                .eventTotalWinners(this.event_total_winners)
                .build();
    }
    
    public String getDdayText() {
        if (this.event_startdate == null || this.event_enddate == null) {
            return "";
        }

        LocalDate today = LocalDate.now();

        // 1. 아직 시작 전인 경우
        if (today.isBefore(this.event_startdate)) {
            long daysUntilStart = ChronoUnit.DAYS.between(today, this.event_startdate);
            return "오픈 D-" + daysUntilStart; // 또는 "진행예정"
        }

        // 2. 이미 종료된 경우
        if (today.isAfter(this.event_enddate)) {
            return "종료";
        }

        // 3. 진행 중인 경우 (종료일까지 남은 일수 계산)
        long remainingDays = ChronoUnit.DAYS.between(today, this.event_enddate);
        if (remainingDays == 0) {
            return "D-Day"; // 오늘 마감
        }
        return "D-" + remainingDays;
    }

    // 상태에 따라 Bootstrap 뱃지 색상 클래스도 함께 내려주면 HTML이 훨씬 깔끔해집니다.
    public String getDdayBadgeClass() {
        String text = getDdayText();
        if ("종료".equals(text)) {
            return "bg-secondary-subtle text-secondary"; // 회색
        } else if (text.startsWith("오픈")) {
            return "bg-info-subtle text-info-emphasis";  // 하늘색
        } else if ("D-Day".equals(text)) {
            return "bg-warning-subtle text-warning-emphasis"; // 주황/노랑 (오늘 마감 강조)
        }
        return "bg-danger-subtle text-danger"; // 기본 빨간색 (D-N)
    }
    
    
}
