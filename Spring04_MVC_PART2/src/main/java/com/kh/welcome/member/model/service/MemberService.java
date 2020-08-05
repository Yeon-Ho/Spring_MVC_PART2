package com.kh.welcome.member.model.service;

import java.util.Map;



import com.kh.welcome.member.model.vo.Member;

public interface MemberService {

	public int insertMember(Member member);
	
	public Member selectMember(Map<String , Object> commandMap);

//------------------회원 수정---------------------------------
	//방법1.
//	public int updateMember(Map<String, Object> commandMap);

	//방법2.
	public int updateMember(Member member);
//-----------------------------------------------------------------	
	
	
//--------------------회원 탈퇴----------------------------------------	
	//방법1.
//	public int deleteMember(Map<String , Object> commandMap);
	
	//방법2. update로 회원 탈퇴
	public int updateMemberToLeave(String userId);
	
//-----------------------------------------------------------------	
	
	public int selectId(String userId);
	
	public void mailSending(Member member ,String urlPath);

	public void updateMemberToLeaveMailSending(Member member, String urlPath);
	
}
