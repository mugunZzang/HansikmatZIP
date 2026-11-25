SHOW user;

SHOW con_name;

SELECT * FROM tab;


show user;

-- 회원 테이블
create table tbl_user    
(userseq             number                       not null   -- 회원번호                
,social_userid       varchar2(50)                            -- 소셜 아이디  
,userid              varchar2(50)                 not null   -- 로그인 아이디                
,passwd              varchar2(200)                not null   -- 비밀번호                
,name                nvarchar2(30)                not null   -- 이름                
,nickname            nvarchar2(50)                not null   -- 닉네임                 
,email               varchar2(200)                not null   -- 이메일                
,mobile              varchar2(200)                not null   -- 연락처                
,email_checked       NUMBER(1)     default 0      not null   -- 이메일 인증 여부(1: 인증완료, 0:미인증)                
,summary             nvarchar2(200)                          -- 자기소개  
,profile_orgfilename varchar2(255)                           -- 프로필 원본 파일명
,profile_sysfilename varchar2(255)                           -- 프로필 저장 파일명   
,constraint PK_TBL_USER_USERSEQ primary key(userseq)
,constraint UQ_TBL_USER_SOCIAL_userid unique(social_userid)
,constraint UQ_TBL_USER_USERID  unique(userid)
,constraint UQ_TBL_USER_EMAIL  unique(email)
);
desc tbl_user;

-- 회원 테이블 시퀀스
create sequence seq_userseq;

-- 권한 테이블
create table tbl_authorities   
(authseq             number                             not null   -- 권한번호                
,fk_userseq          number                             not null   -- 회원번호  
,authority           varchar2(10)  default 'ROLE_USER'  not null   -- 권한                 
,constraint PK_TBL_AUTHORITIES_AUTHSEQ primary key(authseq)
,constraint FK_TBL_AUTHORITIES_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
,constraint UQ_TBL_AUTHORITIES unique(fk_userseq, authority)
,constraint CK_TBL_AUTHORITIES_AUTHORITY CHECK(authority in('ROLE_USER', 'ROLE_CHEF', 'ROLE_ADMIN'))
);

-- 권한 테이블 시퀀스
create sequence seq_authseq;

-- 쉐프 신청 목록 테이블
create table tbl_chef_apply_list   
(chefapplyseq        number                         not null     -- 쉐프 신청 번호                
,fk_userseq          number                         not null     -- 회원번호  
,approval_status     nvarchar2(2)  default '대기'    not null    -- 신청결과 
,rejection_reason    NVARCHAR2(500)                              -- 반려사유 
,applicant_date      DATE          default sysdate  not null     -- 신청일시
,processed_date      DATE                                        -- 처리일시 
,constraint PK_TBL_CHEF_APPLY_LIST_CHEFAPPLYSEQ primary key(chefapplyseq)
,constraint FK_TBL_CHEF_APPLY_LIST_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
,constraint CK_TBL_CHEF_APPLY_LIST_APPROVAL_STATUS CHECK(approval_status IN ('승인', '반려', '대기'))
);

-- 쉐프 신청 목록 테이블 시퀀스
create sequence seq_chefapplyseq;

-- 나만의 냉장고 테이블
create table tbl_refrigerator   
(r_ingredientseq       number             not null     -- 냉장고 재료 번호                
,fk_userseq          number             not null     -- 회원번호  
,r_ingredient_name     NVARCHAR2(50)      not null     -- 재료명 
,constraint PK_TBL_REFRIGERATOR_R_INGREDIENTSEQ primary key(R_INGREDIENTSEQ)
,constraint FK_TBL_REFRIGERATOR_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
,constraint UQ_TBL_REFRIGERATOR unique(fk_userseq, r_ingredient_name)
);

-- 나만의 냉장고 테이블 시퀀스
create sequence seq_r_ingredientseq;

-- 팔로우 테이블
create table tbl_follow   
(follower_userseq      number                   not null     -- 팔로워 회원번호                
,following_userseq     number                   not null     -- 팔로잉 회원번호 
,follow_date           DATE   default sysdate   not null     -- 팔로우 일시 
,constraint PK_TBL_FOLLOW primary key(follower_userseq, following_userseq)
,constraint FK_TBL_FOLLOW_FOLLOWER_USERSEQ  foreign key (follower_userseq) references tbl_user(userseq)
,constraint FK_TBL_FOLLOW_FOLLOWING_USERSEQ foreign key (following_userseq) references tbl_user(userseq)
);

-- 문의사항 테이블
create table tbl_qna   
(qnaseq            number                            not null     -- 문의 번호                
,fk_userseq        number                            not null     -- 회원 번호  
,qna_subject       NVARCHAR2(50)                     not null     -- 제목
,qna_content       NVARCHAR2(500)                    not null     -- 내용 
,qna_status        NVARCHAR2(10)   default '접수'    not null     -- 접수 상태 
,answer            NVARCHAR2(500)                                 -- 답변 내용 
,answer_date       DATE                                           -- 답변 일시 
,question_date     DATE            default sysdate   not null     -- 문의 일시 
,constraint PK_TBL_QNA_QNASEQ primary key(qnaseq)
,constraint FK_TBL_QNA_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
,constraint CK_TBL_QNA_QNA_STATUS CHECK(qna_status IN('접수', '답변완료'))
);

-- 문의사항 테이블 시퀀스
create sequence seq_qnaseq;

-- 문의사항 사진 테이블
create table tbl_qna_images   
(qna_imgseq          number            not null     -- 문의사항 사진 번호                
,fk_qnaseq           number            not null     -- 문의 번호  
,qna_orgfilename     VARCHAR2(255)     not null     -- 사진 원본 파일명 
,qna_sysfilename     VARCHAR2(255)     not null     -- 사진 저장 파일명
,constraint PK_TBL_QNA_IMAGES_QNA_IMGSEQ primary key(qna_imgseq)
,constraint FK_TBL_QNA_IMAGES_FK_QNASEQ foreign key (fk_qnaseq) references tbl_qna(qnaseq)
);

-- 문의사항 사진 테이블 시퀀스
create sequence seq_qna_imgseq;

-- 멤버쉽 테이블
create table tbl_membership   
(mbrseq                number                         not null     -- 멤버쉽 번호                
,fk_userseq            number                         not null     -- 회원번호  
,billingkey            VARCHAR2(255)                  not null     -- 빌링키 
,mbr_status            NUMBER(1)     default 1        not null     -- 상태(1: 활성화, 0: 해지)
,start_membershp_date  DATE          default sysdate  not null     -- 멤버쉽 시작일 
,next_paydate          DATE                           not null     -- 다음 결제 일자 
,constraint PK_TBL_MEMBERSHIP_MBRSEQ primary key(mbrseq)
,constraint FK_TBL_MEMBERSHIP_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
,constraint UK_TBL_MEMBERSHIP_BILLINGKEY unique(billingkey)
);

-- 멤버쉽 테이블 시퀀스
create sequence seq_mbrseq;

-- 레시피 테이블
create table tbl_recipe   
(recipeseq            number                              not null     -- 레시피 번호               
,fk_userseq           number                              not null     -- 회원 번호  
,r_subject            NVARCHAR2(100)                      not null     -- 제목
,r_orgimgname         VARCHAR2(255)                       not null     -- 대표 사진 원본 파일명 
,r_imgname            VARCHAR2(255)                       not null     -- 대표 사진 저장 파일명 
,r_intro              NVARCHAR2(1000)                     not null     -- 레시피 소개 
,r_url                VARCHAR2(500)                                    -- 동영상 주소 
,r_time               NVARCHAR2(10)                       not null     -- 조리 시간 
,r_level              NVARCHAR2(10)                       not null     -- 난이도 
,people               NVARCHAR2(10)                       not null     -- 인원수 
,paid_check           NVARCHAR2(2)                        not null     -- 유료 여부 
,tip_caution          NVARCHAR2(50)                                    -- 팁 및 주의사항 
,r_views              number             default 0        not null     -- 조회수 
,ai_review            NVARCHAR2(500)                                   -- AI 리뷰요약 
,ai_totalcalorie      number                                           -- AI 총 칼로리 계산 
,r_writedate          DATE               default sysdate  not null     -- 작성일시 
,situation_ca         NVARCHAR2(20)                       not null     -- 상황 카테고리명 
,type_ca              NVARCHAR2(20)                       not null     -- 종류 카테고리명 
,main_ca              NVARCHAR2(20)                       not null     -- 주재료 카테고리명 
,recipe_ca            NVARCHAR2(20)                       not null     -- 조리방법 카테고리명 
,view_check           NVARCHAR2(10)                       not null     -- 게시 상태 
,constraint PK_TBL_RECIPE_RECIPESEQ primary key(recipeseq)
,constraint FK_TBL_RECIPE_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
,constraint CK_TBL_RECIPE_R_TIME CHECK(r_time in ('5분이내', '10분이내', '15분이내', '20분이내', '30분이내', '1시간이내', '1시간이상'))
,constraint CK_TBL_RECIPE_R_LEVEL CHECK(r_level in ('요린이', '중급', '상급'   ))
,constraint CK_TBL_RECIPE_PEOPLE CHECK(people in ('1인분', '2인분', '3인분', '4인분 이상'))
,constraint CK_TBL_RECIPE_PAID_CHECK CHECK(paid_check in ('무료','유료'))
,constraint CK_TBL_RECIPE_VIEW_CHECK CHECK(view_check in ('임시저장', '검수대기', '공개'))
);

-- 레시피 테이블 시퀀스
create sequence seq_recipeseq;

-- 이벤트 테이블
create table tbl_event   
(eventseq                   number             not null     -- 이벤트 번호                
,event_subject              NVARCHAR2(100)     not null     -- 제목  
,event_startdate            DATE               not null     -- 시작일
,event_enddate              DATE               not null     -- 종료일 
,event_drawingdate          DATE               not null     -- 발표일
,event_image_org_filename   VARCHAR2(255)      not null     -- 배너이미지 원본 파일명
,event_image_filename       VARCHAR2(255)      not null     -- 배너이미지 저장 파일명 
,event_contents             NVARCHAR2(2000)    not null     -- 내용
,event_total_winners        NUMBER             not null     -- 당첨 정원 
,constraint PK_TBL_EVENT_EVENTSEQ primary key(eventseq)
);

-- 이벤트 테이블 시퀀스
create sequence seq_eventseq;

-- 알림 테이블
create table tbl_notice   
(noticeseq           number                         not null     -- 알림 번호                
,fk_userseq          number                         not null     -- 회원 번호  
,notice_contents     NVARCHAR2(500)                 not null     -- 알림 내용 
,fk_followerseq      NUMBER                                      -- 팔로워 회원 번호
,fk_eventseq         NUMBER                                      -- 이벤트 번호
,fk_recipeseq        NUMBER                                      -- 레시피 번호
,view_status         NUMBER(1)        default 0     not null     -- 열람 여부(1: 열람, 0: 미열람)
,constraint PK_TBL_NOTICE_NOTICESEQ primary key(noticeseq)
,constraint FK_TBL_NOTICE_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
,constraint FK_TBL_NOTICE_FK_FOLLOWERSEQ foreign key (fk_followerseq) references tbl_user(userseq)
,constraint FK_TBL_NOTICE_FK_EVENTSEQ foreign key (fk_eventseq) references tbl_event(eventseq)
,constraint FK_TBL_NOTICE_FK_RECIPESEQ foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade
);
select * from tbl_follow;
-- 알림 테이블 시퀀스
create sequence seq_noticeseq;

-- 이벤트 참가 명단 테이블
create table tbl_entry   
(entryseq       number                  not null     -- 참가 번호                
,fk_eventseq    number                  not null     -- 이벤트 번호  
,fk_userseq     NUMBER                  not null     -- 회원 번호 
,fk_recipeseq   NUMBER                  not null     -- 레시피 번호
,chk_win        NUMBER(1) default 0     not null     -- 당첨 여부(1: 당첨, 0: 미당첨)
,constraint PK_TBL_ENTRY_ENTRYSEQ primary key(entryseq)
,constraint FK_TBL_ENTRY_FK_EVENTSEQ foreign key (fk_eventseq) references tbl_event(eventseq)
,constraint FK_TBL_ENTRY_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
,constraint FK_TBL_ENTRY_FK_RECIPESEQ foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade
,constraint UK_TBL_ENTRY unique(fk_userseq, fk_eventseq)
);

-- 이벤트 참가 명단 테이블 시퀀스
create sequence seq_entryseq;

-- 레시피 검수 이력 테이블
create table tbl_recipe_reveal_status   
(inspectionseq           number           not null     -- 레시피 검수 번호               
,fk_recipeseq            number           not null     -- 레시피 번호  
,inspection_status       NVARCHAR2(2)     not null     -- 검수 결과 
,reject_reason           NVARCHAR2(500)                -- 반려 사유 
,inspection_processed_at DATE             not null     -- 처리 일시 
,constraint PK_tbl_recipe_reveal_status_inspectionseq primary key(inspectionseq)
,constraint FK_tbl_recipe_reveal_status_fk_recipeseq foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade
,constraint CK_tbl_recipe_reveal_status CHECK( inspection_status IN ('반려', '승인')) 
);

-- 레시피 검수 이력 테이블 시퀀스
create sequence seq_inspectionseq;

-- 레시피 댓글 테이블
create table tbl_recipe_comment   
(commentseq           number                         not null     -- 댓글번호                
,fk_recipeseq         number                         not null     -- 레시피번호
,fk_userseq           number                         not null     -- 회원번호
,parent_commentseq    number                                      -- 상위댓글번호
,comment_content      NVARCHAR2(500)                 not null     -- 내용
,comment_created_at   DATE           default sysdate not null     -- 작성일시
,constraint PK_tbl_recipe_comment_commentseq primary key(commentseq)
,constraint FK_tbl_recipe_comment_fk_userseq foreign key (fk_userseq) references tbl_user(userseq)
,constraint FK_tbl_recipe_comment_fk_recipeseq foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade
,constraint FK_tbl_recipe_comment_parent_commentseq foreign key (parent_commentseq) references tbl_recipe_comment(commentseq) on delete cascade -- 상위 댓글 참조
);
 
-- 레시피 댓글 테이블 시퀀스
create sequence seq_commentseq;

-- 레시피 리뷰 테이블
create table tbl_recipe_review   
(reviewseq                number                          not null     -- 리뷰번호                
,fk_recipesesq            number                          not null     -- 레시피번호  
,fk_userseq               number                          not null     -- 회원번호 
,review_rating            number                          not null     -- 별점 
,review_content           NVARCHAR2(500)                  not null     -- 내용 
,review_origin_file_name  VARCHAR2(255)                                -- 리뷰 사진 원본 파일명 
,review_file_name         VARCHAR2(255)                                -- 리뷰 사진 저장 파일명 
,review_reply_content     NVARCHAR2(500)                               -- 작성자 답글 내용 
,review_reply_created_at  DATE                                         -- 작성자 답글 일시 
,review_created_at        DATE            default sysdate not null     -- 작성 일시 
,constraint PK_TBL_RECIPE_REVIEW_REVIEWSEQ primary key(reviewseq)
,constraint FK_TBL_RECIPE_REVIEW_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
,constraint FK_TBL_RECIPE_REVIEW_FK_RECIPESESQ foreign key (fk_recipesesq) references tbl_recipe(recipeseq) on delete cascade
,constraint CK_TBL_RECIPE_REVIEW_REVIEW_RATING CHECK( review_rating IN (1, 2, 3, 4, 5))
);

-- 레시피 리뷰 테이블 시퀀스
create sequence seq_reviewseq;

-- 스크랩 폴더 테이블
create table tbl_scrap_folder   
(folderseq       number                           not null     -- 폴더번호                
,fk_userseq      number                           not null     -- 회원번호  
,folder_name     NVARCHAR2(20)  default '보따리'   not null     -- 폴더명 
,constraint PK_TBL_SCRAP_FOLDER_FOLDERSEQ primary key(folderseq)
,constraint FK_TBL_SCRAP_FOLDER_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
);

-- 스크랩 폴더 테이블 시퀀스
create sequence seq_folderseq;

-- 스크랩 레시피 테이블
create table tbl_scrap_recipe   
(fk_recipeseq      number     not null     -- 레시피번호                
,fk_folderseq      number     not null     -- 폴더번호  
,constraint PK_TBL_SCRAP_RECIPE primary key(fk_recipeseq, fk_folderseq)
,constraint FK_TBL_SCRAP_RECIPE_FK_RECIPESEQ foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade
,constraint FK_TBL_SCRAP_RECIPE_FK_FOLDERSEQ foreign key (fk_folderseq) references tbl_scrap_folder(folderseq) on delete cascade
);

-- 노트 테이블
create table tbl_note   
(fK_userseq      number                            not null     -- 회원번호               
,fk_recipeseq    number                            not null     -- 레시피번호  
,n_comment       NVARCHAR2(1000)                   not null     -- 노트내용 
,n_writedate     DATE             default sysdate  not null     -- 작성일시
,constraint PK_TBL_NOTE primary key(fK_userseq, fk_recipeseq)
,constraint FK_TBL_NOTE_FK_USERSEQ foreign key (fk_userseq) references tbl_user(userseq)
,constraint FK_TBL_NOTE_FK_RECIPESEQ foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade
);

-- 레시피 태그 테이블
create table tbl_recipe_tag  
(tag_name       NVARCHAR2(20)   not null     -- 태그명                
,fk_recipeseq   number          not null     -- 레시피 번호  
,constraint PK_TBL_RECIPE_TAG primary key(tag_name, fk_recipeseq)
,constraint FK_TBL_RECIPE_TAG_FK_RECIPESEQ foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade
);

-- 재료 묶음 테이블
create table tbl_ingredient_bundle   
(ingredient_bundleseq       number             not null     -- 재료묶음번호               
,fk_recipeseq               number             not null     -- 레시피번호  
,ingredient_bundle_name     NVARCHAR2(20)      not null     -- 재료묶음명
,ingredient_bundle_order    number                          -- 노출순서 
,constraint PK_TBL_INGREDIENT_BUNDLE_INGREDIENT_BUNDLESEQ primary key(ingredient_bundleseq)
,constraint FK_TBL_INGREDIENT_BUNDLE_FK_RECIPESEQ foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade 
);

-- 재료 묶음 테이블 시퀀스
create sequence seq_ingredient_bundleseq;

-- 재료 테이블
create table tbl_ingredient   
(ingredientseq             number           not null     -- 재료번호                
,fk_ingredient_bundleseq   number           not null     -- 재료묶음번호  
,ingredient_name           NVARCHAR2(20)    not null     -- 재료명 
,unit                      NVARCHAR2(10)    not null     -- 단위 
,quantity                  NUMBER           not null     -- 수량 
,calorie                   NVARCHAR2(10)                 -- 칼로리 
,ingredient_order          NUMBER           not null     -- 노출순서 
,ing_note                  NVARCHAR2(20)                 -- 비고 
,constraint PK_TBL_INGREDIENT_INGREDIENTSEQ primary key(ingredientseq)
,constraint FK_TBL_INGREDIENT_FK_INGREDIENT_BUNDLESEQ foreign key (fk_ingredient_bundleseq) references tbl_ingredient_bundle(ingredient_bundleseq) on delete cascade
);

-- 재료 테이블 시퀀스
create sequence seq_ingredientseq;

-- 레시피 요리순서 테이블
create table tbl_cook_order   
(cook_orderseq           number             not null     -- 요리순서번호                
,fk_recipeseq            number             not null     -- 레시피번호  
,cook_order_step         NUMBER             not null     -- 단계순서 
,cook_description        NVARCHAR2(500)     not null     -- 요리설명
,cook_original_filename  VARCHAR2(255)                   -- 순서 사진 원본 파일명
,cook_stored_filename    VARCHAR2(255)                   -- 순서 사진 저장 파일명
,order_tip               NVARCHAR2(50)                   -- 순서 팁
,timer                   NUMBER                          -- 타이머 초
,constraint PK_TBL_COOK_ORDER_COOK_ORDERSEQ primary key(cook_orderseq)
,constraint FK_TBL_COOK_ORDER_FK_RECIPESEQ foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade
);

-- 레시피 요리순서 테이블 시퀀스
create sequence seq_cook_orderseq;

-- 레시피 완성 사진 테이블
create table tbl_finished_image   
(finished_imageseq        number             not null     -- 완성 사진 번호               
,fk_recipeseq             number             not null     -- 레시피 번호  
,rcp_original_file_name   VARCHAR2(255)      not null     -- 사진 원본 파일명
,rcp_stored_file_name     VARCHAR2(255)      not null     -- 사진 저장 파일명 
,constraint PK_TBL_FINISHED_IMAGE_FINISHED_IMAGESEQ primary key(finished_imageseq)
,constraint FK_TBL_FINISHED_IMAGE_FK_RECIPESEQ foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade
);

-- 레시피 완성사진 테이블 시퀀스
create sequence seq_finished_imageseq;

-- 레시피 랭킹 테이블
create table tbl_recipe_rank   
(recipe_checkseq    number                      not null     -- 조회번호                
,fk_recipeseq       number                      not null     -- 레시피번호 
,c_date             DATE     default sysdate    not null     -- 일자
,c_views            number   default 1          not null     -- 조회수
,constraint PK_TBL_RECIPE_RANK_RECIPE_CHECKSEQ primary key(recipe_checkseq)
,constraint FK_TBL_RECIPE_RANK_FK_RECIPESEQ foreign key (fk_recipeseq) references tbl_recipe(recipeseq) on delete cascade
);

-- 레시피 랭킹 테이블 시퀀스
create sequence seq_recipe_checkseq;

-- 검색어 테이블
create table tbl_searchname   
(searchseq       number             not null     -- 검색어 번호                
,searchname      NVARCHAR2(50)      not null     -- 검색어 
,constraint PK_TBL_SEARCHNAME_SEARCHSEQ primary key(searchseq)
,constraint UK_TBL_SEARCHNAME_SEARCHNAME unique(searchname)
);

-- 검색어 테이블 시퀀스
create sequence seq_searchseq;

-- 검색어 랭킹 테이블
create table tbl_search_rank   
(search_checkseq   number                     not null     -- 조회 번호                
,fk_searchseq      number                     not null     -- 검색어 번호  
,s_date            DATE     default sysdate   not null     -- 일자 
,s_views           number   default 1         not null     -- 조회수
,constraint PK_tbl_search_rank_search_checkseq primary key(search_checkseq)
,constraint FK_tbl_search_rank_fk_searchseq foreign key (fk_searchseq) references tbl_searchname(searchseq)
);

insert into tbl_user(userseq, userid, passwd, name, nickname, email, mobile, email_checked, summary, profile_orgfilename, profile_sysfilename)
values(SEQ_USERSEQ.nextval, 'admin_knowhow', '1234', '관리자', '장금이의 비법', 'admin1@test.com', '010-1234-1234', 1, null, null, 'default.png');
commit;



select * from tab;
select * from user_sequences;
select * from EMBEDDING_TEXT_RECIPE;

admin_knowhow

select * from tbl_user;
SELECT * FROM  TBL_RECIPE;

nickname, profile_sysfilename


select * from tbl_event;
select * from tbl_entry;

select * from tbl_event;