package com.kh.welcome.member.model.service;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.kh.welcome.member.model.dao.MemberDao;
import com.kh.welcome.member.model.vo.Member;

@Service
//@Service : @Controller 나 @Repository와 달리
//		bean으로 등록시켜주는 기능 외에는 별다른 기능이 없다
//		@Component와 동일, 단 가독성을 위해 @Service 어노테이션을 사용한다.
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	MemberDao memberDao;

	@Autowired
	JavaMailSender mailSender;
	
	public int insertMember(Member member) {
		return memberDao.insertMember(member);
	}
	
	public Member selectMember(Map<String , Object> commandMap) {
		return memberDao.selectMember(commandMap);
	}

//------------------회원 수정---------------------------------
	//방법1.
//	public int updateMember(Map<String, Object> commandMap) {
//		return memberDao.updateMember(commandMap);
//	}

	//방법2.
	public int updateMember(Member member) {
		return memberDao.updateMember(member);
	}
//-----------------------------------------------------------------	
	
	
//--------------------회원 탈퇴----------------------------------------	
	//방법1.
//	public int deleteMember(Map<String , Object> commandMap) {
//		return memberDao.deleteMember(commandMap);
//	}
	
	//방법2. update로 회원 탈퇴
	public int updateMemberToLeave(String userId) {
		
		return memberDao.updateMemberToLeave(userId);
	}
	
	
//-----------------------------------------------------------------	
	public int selectId(String userId) {
		return memberDao.selectId(userId);
	}
	
	public void mailSending(Member member ,String urlPath) {
		
		String setfrom = "fksor12365@naver.com";
		String tomail = member.getEmail();
		String title = "회원가입을 환영합니다.";
		String htmlBody = "<form "
		         + "action='http://"+urlPath+"/member/joinimpl.do'"
		         +" method='post'>"
		         + "<h3>회원가입을 환영합니다</h3>"
		         + "<input type='hidden' value='" 
		               + member.getUserId() 
		               + "' name='userId'>"
		         + "<input type='hidden' value='"
		               + member.getPassword()
		               +"' name='password'>"
		         + "<input type='hidden' value='"
		               + member.getTell()
		               +"' name='tell'>"
		         + "<input type='hidden' value='"
		               + member.getEmail()
		               +"' name='email'>"
		         + "<button type='submit'>전송하기</button></form>";

		
		 mailSender.send(new MimeMessagePreparator() {
			   public void prepare(MimeMessage mimeMessage) throws MessagingException {
			     MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			     //보내는 이
			     message.setFrom(setfrom);
			     //받는 이
			     message.setTo(tomail);
			     //메일 제목
			     message.setSubject(title);
			     //메일 내용
			     // 두번째 매개변수 boolean값은 html인지 아닌지의 여부 
			     message.setText(htmlBody ,true);
			   }
		 });
	}

	public void updateMemberToLeaveMailSending(Member member, String urlPath) {
		
		String setfrom = "fksor12365@naver.com";
		String tomail = member.getEmail();
		String title = "회원탈퇴.";
		String htmlBody = "<h1>회원탈퇴처리가 완료되었습니다.</h3>";

		
		 mailSender.send(new MimeMessagePreparator() {
			   public void prepare(MimeMessage mimeMessage) throws MessagingException {
			     MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			     //보내는 이
			     message.setFrom(setfrom);
			     //받는 이
			     message.setTo(tomail);
			     //메일 제목
			     message.setSubject(title);
			     //메일 내용
			     // 두번째 매개변수 boolean값은 html인지 아닌지의 여부 
			     message.setText(htmlBody ,true);
			   }
		 });
	}
	
}
