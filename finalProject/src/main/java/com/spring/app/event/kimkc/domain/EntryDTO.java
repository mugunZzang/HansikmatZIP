package com.spring.app.event.kimkc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryDTO {
	
	private Long entryseq;
    private Long fk_eventseq;
    private Long fk_userseq;
    private Long fk_recipeseq;
    private Integer chk_win; // 1: 당첨, 0: 미당첨

    // --- 조인 조회 및 화면 출력용 추가 필드 ---
    private String event_subject;
    private String nickname;
    private String profile_sysfilename;
    private String r_subject;
    private String r_imgname;
}
