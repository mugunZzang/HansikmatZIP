package com.spring.app.admin.event.jungyj.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventEntryDTO {

	private Integer entryseq;
    private Integer fk_eventseq;
    private Integer fk_userseq;
    private Integer fk_recipeseq;
    private int chk_win;

    // 사용자 닉네임 필드(조회용)
    private String nickname;
    // 참여한 레시피 제목 필드(조회용)
    private String r_subject;
    // 참여한 레시피 이미지 필드(조회용)
    private String r_imgname;
}
