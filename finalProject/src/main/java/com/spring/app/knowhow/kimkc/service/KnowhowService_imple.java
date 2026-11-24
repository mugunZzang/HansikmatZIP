package com.spring.app.knowhow.kimkc.service;

import static com.spring.app.kimkc.entity.QRecipe.recipe;
import static com.spring.app.kimkc.entity.QUser.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.app.kimkc.entity.Recipe;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KnowhowService_imple implements KnowhowService {
	
	// 레퍼지터리 만들기?
	
	
	private final JPAQueryFactory jPAQueryFactory;
	
	// 현재 페이지에 해당하는 노하우 리스트 불러오기
	@Override
	public Page<Recipe> getPageKnowhow(int currentShowPageNo_int, int sizePerPage) {

//		Page<Board> page = null;
		Page<Recipe> page = Page.empty(); // 기본값으로 내용이 없는 빈 페이지임. null 아니므로 안전하게 메서드 호출 가능함.
        								 // 검색 결과가 없을 때, 기본값으로 반환
		
		/*   Spring Data (spring-data-commons 라이브러리)에서 제공하는 인터페이스인 Page 는  
	       	페이징 처리된 데이터 및 페이지 정보(총 페이지수, 전체 아이템 개수, 현재 페이지 번호, 현재 페이지의 아이템 등등)를 
	       	제공해주는 인터페이스이다.

	       	!!! Page 객체는 일반적으로 Repository의 조회 메서드(findAll(), findBy필드명, 등등)에서 Pageable 객체를 파라미터로 받아서 결과물을 반환할 때 사용된다. !!! 
	   */
		
		System.out.println("전달받은 페이지 번호: " + currentShowPageNo_int);
		System.out.println("계산된 인덱스: " + (currentShowPageNo_int - 1));
		System.out.println("페이지 크기: " + sizePerPage);
		
		Pageable pageable = PageRequest.of(currentShowPageNo_int-1, sizePerPage, Sort.by(Sort.Direction.DESC, "recipeseq"));
		
		
		// 노하우는 관리자 중 닉네임이 "장금이의 비법" 인 관리자가 작성한 레시피만 해당된다.
		BooleanExpression condition = Expressions.TRUE;
		condition = condition.and(recipe.user.nickname.eq("장금이의 비법"));
		
		
		// 노하우 리스트 갖고오기
		List<Recipe> knowhowList = jPAQueryFactory
				.selectFrom(recipe)
				.join(recipe.user, user).fetchJoin()
				//.where(condition)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(recipe.recipeSeq.desc())
				.fetch();
		
		// 총 노하우 수
		Long total = jPAQueryFactory
        		.select(recipe.count())
        		.from(recipe)
                .join(recipe.user, user)
                //.where(condition)
                .fetchOne();
				
		page = new PageImpl<>(knowhowList, pageable, total != null ? total : 0);		
		
		return page;
	}

}
