package com.spring.app.event.kimkc.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventListDTO {

	private Long eventseq;
    private String event_subject;
    private LocalDate event_startdate;
    private LocalDate event_enddate;
    private LocalDate event_drawingdate;
    private String event_image_org_filename;
    private String event_image_filename;
    private String event_contents;
    private Integer event_total_winners;

    // --- 화면 출력용 추가 필드 ---
    private String startDateStr;   // 예: "2026.11.18"
    private String endDateStr;     // 예: "2026.12.15"
    private String drawingDateStr; // 예: "2026.12.20"
    private Long dDay;             // 남은 일수 (0이면 D-DAY, 음수면 종료)
	
}
