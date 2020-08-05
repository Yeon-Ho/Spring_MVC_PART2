package com.kh.welcome.member.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.welcome.member.model.vo.Member;

@Repository
//@Repository 어노테이션
// 해당 클래스를 Dao 역할을 하는 Bean으로 등록한다.
// SqlException을 DataAccessException으로 전환한다.
public class MemberDao {

	@Autowired
	SqlSessionTemplate session;
	
	//select 쿼리 
	//	1.결과가 하나인 경우 sqlSessionTemplate.selectOne(네임스페이스.태그id , 매핑시킬 객체)
	//  2.결과가 여러개인 경우 sqlSessionTemplate.selectList(네임스페이스.태그id , 매핑시킬 객체)
	
	//insert 쿼리 sqlSessionTemplate.insert(네임스페이스.태그id , 매핑시킬 객체)
	//update 쿼리 sqlSessionTemplate.update(네임스페이스.태그id , 매핑시킬 객체)
	//delete 쿼리 sqlSessionTemplate.delete(네임스페이스.태그id , 매핑시킬 객체)
	
	//  ** 매핑시킬 객체가 필요하지 않다면 두번째 매개변수를 생략 가능
	
	
	public int insertMember(Member member) {
		//MEMBER.insertMember : 
		// Mapper의 NameSpace이름 , 태그의 id 속성 값
		return session.insert("MEMBER.insertMember" , member);
	}

	public Member selectMember(Map<String , Object> commandMap) {
		
		return session.selectOne("MEMBER.selectMember", commandMap);
	}

//-----------------회원 수정---------------------------------
	//방법1
//	public int updateMember(Map<String, Object> commandMap) {
//		return session.update("MEMBER.updateMember", commandMap);
//	}
	
	//방법2
	public int updateMember(Member member) {
		return session.update("MEMBER.updateMember", member);
	}
//-----------------------------------------------------------------

	
	
//------------------회원 탈퇴----------------------------------
//	//방법1
//	public int deleteMember(Map<String , Object> commandMap) {
//		return session.delete("MEMBER.deleteMember", commandMap);
//	}
	
	//방법2 - 업데이트로 처리하기
	public int updateMemberToLeave(String userId) {
		return session.update("MEMBER.leaveMember",userId);
	}
	
//-----------------------------------------------------------------
	
	//--------------중복 확인 -----------------------------------
	public int selectId(String userId) {
		return session.selectOne("MEMBER.selectId",userId);
	}
	
}
