package com.spring.app.begin.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;



@Repository
@RequiredArgsConstructor
public class BeginDAO_imple implements BeginDAO {

	@Qualifier("sqlsession")
	private final SqlSessionTemplate sqlsession;

	@Override
	public int selectSome() {
		return sqlsession.selectOne("begin.selectSome");
	}

	
	
}
