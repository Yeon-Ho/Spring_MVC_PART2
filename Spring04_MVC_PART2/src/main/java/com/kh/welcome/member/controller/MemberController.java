package com.kh.welcome.member.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kh.welcome.member.model.service.MemberService;
import com.kh.welcome.member.model.vo.Member;

@Controller
//Controller 어노테이션으로 해당 객체를 WebApplicationContext에 등록
//  Controller 어노테이션을 등록함으로써 @RequestParam , @RequestMapping
//	같은 Controller를 위한 어노테이션을 사용할 수 있다.

@RequestMapping("member")
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	
//	방법 1.
	@RequestMapping(value="/login.do")
	public void login() {
		
	}
	
//	//방법 2.
//	@RequestMapping("/login.do")
//	//RequestMapping을 class에 적용하면 해당 클래스의 모든 메소드에 적용되는
//	//url을 지정할수 있다. 이 url 메소드에 붙는 url 앞 경로를 의미한다.
//	public String login() {
//		//String이 반환형이면 String으로 반환되는 문자열이 jsp의 경로가 된다.
//		return "/member/login";
//	}
	
//	//방법 3.
//	@RequestMapping(value="/login.do",method=RequestMethod.GET)
//	//value에 url , method 속성에 허가할 http method 방식 지정
//	public ModelAndView login() {
//		//ModelAndView : model객체에 값을 담고
//		//ViewName을 보내고 싶은 jsp의 경로로 지정
//
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("/member/login");
//		return mav;
//	}

	//-----------------------회원 가입-----------------------------------------------------------------
	//방법 1.
//	@RequestMapping(value="/join.do" , method=RequestMethod.GET)
//	//해당 메소드를 어떤 url에 매핑 시킬 지 설정
//	//value 속성에 url을 지정할 수 있다 . 단 지정할 속성이 value밖에 없다면 생략 가능하다.
//	public void join() {
//		//void : 컨트롤러에서 리턴타입이 void라면 요청온 url과 동일한
//		//		 jsp를 찾아서 사용자에게 보내준다.
//	}
	
	//방법 2.
	@RequestMapping("/join.do")
	//RequestMapping을 class에 적용하면 해당 클래스의 모든 메소드에 적용되는
	//url을 지정할수 있다. 이 url 메소드에 붙는 url 앞 경로를 의미한다.
	public String join() {
		//String이 반환형이면 String으로 반환되는 문자열이 jsp의 경로가 된다.
		return "/member/join";
	}
	
	//방법 3.
//	@RequestMapping(value="/join.do",method=RequestMethod.GET)
//	//value에 url , method 속성에 허가할 http method 방식 지정
//	public ModelAndView join() {
//		//ModelAndView : model객체에 값을 담고
//		//ViewName을 보내고 싶은 jsp의 경로로 지정
//
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("/member/join");
//		return mav;
//	}
	//-------------------------------------------------------------------------------------------
	
	@RequestMapping("/joinimpl.do")
	//@RequestParam : http 요청 파라미터를 컨트롤러 메서드의 파라미터로 전달받을 때 사용한다.
	// 타입을 자동으로 변환해준다(매개변수의 타입을 int로 지정했다면 String을 int로 변환해준다)
	// 단 변경이 불가능한 경우에는 400코드를 반환한다.
	public ModelAndView joinImpl(
			
			//방법1
			//@RequestParam Map<String , Object> commandMap
			
			//방법2
			@ModelAttribute Member member,
			HttpServletRequest req
			) {
		
		String root = req.getContextPath();
				
		ModelAndView mav = new ModelAndView();
//		System.out.println(member);
		int res = memberService.insertMember(member);
		
		if(res>0) {
			mav.addObject("alertMsg", "회원가입에 성공하였습니다.");
			mav.addObject("url",root+"/member/login.do");
			mav.setViewName("/common/result");
		}else {
			mav.addObject("alertMsg" , "회원가입에 실패하였습니다.");
			mav.addObject("url", root + "/member/join.do");
			mav.setViewName("/common/result");
		}
		
		return mav;
	}
	
	
	@RequestMapping(value="/loginimpl.do" ,method=RequestMethod.POST)
	public ModelAndView loginimpl(@RequestParam Map<String , Object> commandMap , HttpSession session , HttpServletRequest req) {
		

		ModelAndView mav = new ModelAndView();
		Member res = memberService.selectMember(commandMap);
		
		if(res != null) {
			session.setAttribute("logInInfo", res);
			mav.addObject("alertMsg","로그인에 성공했습니다.");
			mav.addObject("url", req.getContextPath()+"/member/mypage.do");
			mav.setViewName("common/result");
		}else {
			mav.addObject("alertMsg", "로그인에 실패했습니다.");
			mav.addObject("url",req.getContextPath()+"/member/login.do");
			mav.setViewName("common/result");
		}
		
		
		return mav;
	}
	
	//---- 마이페이지 보여주기 -------------------
	@RequestMapping("/mypage.do")
	public void mypage() {

	}
	//--------------------------------------------------------
	
	
	//--------------------------회원  수정---------------------------------------------------------
	//방법1
//	@RequestMapping("/modify.do")
//	public ModelAndView modify(@RequestParam Map<String , Object> commandMap , HttpSession session , HttpServletRequest req) {
//		
//		ModelAndView mav = new ModelAndView();
//		System.out.println("변경값 : " +commandMap);
//		int res = memberService.updateMember(commandMap);
//		
//		if(res > 0) {
//			
//			mav.addObject("alertMsg","회원수정에 성공했습니다.");
//			mav.addObject("url", req.getContextPath()+"/member/mypage.do");
//			mav.setViewName("common/result");
//			
//			Member member = (Member) session.getAttribute("logInInfo");
//			
//			member.setPassword((String)commandMap.get("password"));
//			member.setEmail((String)commandMap.get("email"));
//			member.setTell((String)commandMap.get("tell"));
//
//			session.setAttribute("logInInfo", member);
//		}else {
//			mav.addObject("alertMsg", "회원수정에 실패했습니다.");
//			mav.addObject("url",req.getContextPath()+"/member/mypage.do");
//			mav.setViewName("common/result");
//		}
//		return mav;
//	}
	
	//방법2
	@RequestMapping("/modify.do")
	public String modify(
			//@ModelAttribute 어노테이션은 생략 가능
			Member member 
			,HttpSession session 
			//ModelAndView 에서 값을 저장하는 Model객체
			// view 경로를 지정하는 것은 return하는 String에 지정한다.
			,Model model) {
		
		Member sessionMember = (Member) session.getAttribute("logInInfo");
		member.setUserId(sessionMember.getUserId());
		
		int res = memberService.updateMember(member);
		
		//회원 수정에 성공했다면
		if(res > 0) {
			//Model.addAtrribute(K,V)	: view에 전달할 데이터를 추가하는 메서드
			model.addAttribute("alertMsg", "회원정보 수정에 성공하였습니다.");
			model.addAttribute("url","mypage.do");
			
			sessionMember.setPassword(member.getPassword());
			sessionMember.setEmail(member.getEmail());
			sessionMember.setTell(member.getTell());
			
			
		}else {
			model.addAttribute("alerMsg", "회원정보 수정에 실패하였습니다.");
			model.addAttribute("url", "mypage.do");
		}
		
		return "common/result";
	}
	//----------------------------------------------------------------------------------------------------------------------------
	
	
	
	
	
	//---------------------------회원 탈퇴 -----------------------------------------------------------------------
	//방법 1
//	@RequestMapping("/leave.do")
//	public ModelAndView leave(@RequestParam Map<String , Object> commandMap , HttpSession session , HttpServletRequest req) {
//		ModelAndView mav = new ModelAndView();
//		
//		int res = memberService.deleteMember(commandMap);
//		if(res > 0) {
//			
//			mav.addObject("alertMsg","회원탈퇴에 성공했습니다.");
//			mav.addObject("url", req.getContextPath()+"/member/login.do");
//			mav.setViewName("common/result");
//			
//		}else {
//			mav.addObject("alertMsg", "회원탈퇴에 실패했습니다.");
//			mav.addObject("url",req.getContextPath()+"/member/mypage.do");
//			mav.setViewName("common/result");
//		}
//		
//		return mav;
//	}
	
	//방법2 update로 회원탈퇴
	@RequestMapping("/leave.do")
	public String memberLeave(HttpSession session, Model model ,HttpServletRequest req) {
		Member member = (Member) session.getAttribute("logInInfo");
		
		
		int res = memberService.updateMemberToLeave(member.getUserId());
		
		String urlPath = req.getServerName()+":"+req.getServerPort()+req.getContextPath();
		
		if(res > 0) {
			memberService.updateMemberToLeaveMailSending(member,urlPath);
			model.addAttribute("alertMsg" , "회원탈퇴하셨습니다.");
			model.addAttribute("url", "login.do");
			session.removeAttribute("logInInfo");
		}else {
			model.addAttribute("alertMsg" , "에러가 발생하였습니다.");
			model.addAttribute("url","login.do");
		}
				
		return "common/result";
		
	}
	//----------------------------------------------------------------------------------------------------------------------------
	
	//--------------아이디 중복 찾기----------------------------------------------------------------------------------------------------------
	@RequestMapping("/idcheck.do")
	@ResponseBody
//	@ResponseBody() : 메소드에 	@ResponseBody() 를 지정하면
//	메소드에서 리턴하는 값을 viewResolver를 거쳐서 출력하지 않고
//	Http Response Body 에 직접 쓰게 된다.
	public String idCheck(String userId) {
		System.out.println("idCheck메소드가 호출되었습니다.");
		System.out.println("id : "+ userId);
		
		int res = memberService.selectId(userId);
		
		if(res > 0) {
			return userId;
		}else {
			return "";
		}
		
	}
	//----------------------------------------------------------------------------------------------------------------------------

	//---------------------이메일------------------------------------------------------------------------------------------
	
	@RequestMapping("/joinemailcheck.do")
	public ModelAndView joinEmailCheck(Member member , HttpServletRequest req) {
		
		ModelAndView mav = new ModelAndView();
		String urlPath = req.getServerName()+":"+req.getServerPort()+req.getContextPath();
		
		memberService.mailSending(member, urlPath);
		
		mav.addObject("alertMsg", "이메일로 확인 메일이 발송 되었습니다.");
		mav.addObject("url","login.do");
		mav.setViewName("common/result");
		
		return mav;
	}
	
	
	//--------------로그아웃
	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {
		session.removeAttribute("logInInfo");
		
		return "redirect:login.do";
	}



}
